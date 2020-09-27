package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.exception.NoValidConstructorException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;

/**
 * Does the job of actually creating real objects
 */
class RealObjectMaker  {
  private final InjectionConfig mInjectionConfig;

  RealObjectMaker(InjectionConfig injectionConfig) {
    mInjectionConfig = injectionConfig;
  }

  <T> T createObject(DependencyProvider dependencyProvider, TypeToken<T> typeToken) {
    try {
      return createObjectInternal(dependencyProvider, typeToken);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  void injectObject(DependencyProvider dependencyProvider, Object instance, TypeToken<?> typeToken) {
    try {
      assignInjectableFields(dependencyProvider, instance, typeToken);
      callInjectableMethods(dependencyProvider, instance, typeToken);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T createObjectInternal(DependencyProvider dependencyProvider, TypeToken<T> typeToken) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<T> constructor = mInjectionConfig.chooseConstructor(typeToken);
    if (constructor == null) {
      throw new NoValidConstructorException(typeToken);
    }

    int paramCount = constructor.getParameterCount();
    Type[] paramTypes = constructor.getGenericParameterTypes();
    Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
    Object[] paramValues = new Object[paramCount];

    for (int i = 0; i<paramCount; i++) {
      paramValues[i] = createDependency(
          dependencyProvider,
          paramTypes[i],
          paramAnnotations[i],
          String.format("Constructor (%s)", typeToken),
          typeToken);
    }

    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }

    T instance = constructor.newInstance(paramValues);
    injectObject(dependencyProvider, instance, typeToken);
    return instance;
  }

  private Object createDependency(
      DependencyProvider dependencyProvider,
      Type paramType,
      Annotation[] paramAnnotations,
      String description,
      TypeToken<?> context) {
    TypeToken<?> paramToken = context.resolveType(paramType);
    @Nullable Annotation qualifierAnnotation = ReflectUtil.findQualifierAnnotation(
        paramAnnotations,
        String.format("%s, Param (%s)", description, paramToken));
    DependencyKey<?> paramKey = DependencyKey.of(paramToken, qualifierAnnotation);
    return dependencyProvider.get(paramKey);
  }

  private void assignInjectableFields(DependencyProvider dependencyProvider, Object instance, TypeToken<?> context) throws IllegalAccessException {
    List<Class<? extends Annotation>> injectFieldAnnotations = mInjectionConfig.provideInjectableFieldAnnotations();
    if (injectFieldAnnotations.isEmpty()) {
      return;
    }

    for (Field field : ReflectUtil.getAllDeclaredFields(instance.getClass())) {
      if (ReflectUtil.isAnyAnnotationPresent(field, injectFieldAnnotations)) {
        DependencyKey<?> paramKey = DependencyKey.fromField(field, context);
        Object paramValue = dependencyProvider.get(paramKey);
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        field.set(instance, paramValue);
      }
    }
  }

  private void callInjectableMethods(DependencyProvider dependencyProvider, Object instance, TypeToken<?> context) {
    List<Class<? extends Annotation>> injectMethodAnnotations = mInjectionConfig.provideInjectableMethodAnnotations();
    if (injectMethodAnnotations.isEmpty()) {
      return;
    }

    for (Method method : ReflectUtil.getAllDeclaredMethods(instance.getClass())) {
      if (ReflectUtil.isAnyAnnotationPresent(method, injectMethodAnnotations)) {
        invokeMethod(method, instance, dependencyProvider, context);
      }
    }
  }

  private void invokeMethod(Method method, Object instance, DependencyProvider dependencyProvider, TypeToken<?> context) {
    int paramCount = method.getParameterCount();
    Type[] paramTypes = method.getGenericParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    Object[] paramValues = new Object[paramCount];

    for (int i = 0; i< paramCount; i++) {
      paramValues[i] = createDependency(
          dependencyProvider,
          paramTypes[i],
          paramAnnotations[i],
          String.format("Method (name: %s, during creation of: %s)", method, instance.getClass().getSimpleName()),
          context);
    }

    if (!method.isAccessible()) {
      method.setAccessible(true);
    }

    try {
      method.invoke(instance, paramValues);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
