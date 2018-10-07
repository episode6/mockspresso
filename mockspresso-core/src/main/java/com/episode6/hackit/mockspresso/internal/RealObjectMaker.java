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
  private final InjectionConfig.ConstructorSelector mConstructorSelector;
  private final List<Class<? extends Annotation>> mInjectFieldAnnotations;
  private final List<Class<? extends Annotation>> mInjectMethodAnnotations;

  RealObjectMaker(
      InjectionConfig.ConstructorSelector constructorSelector,
      List<Class<? extends Annotation>> injectFieldAnnotations,
      List<Class<? extends Annotation>> injectMethodAnnotations) {
    mConstructorSelector = constructorSelector;
    mInjectFieldAnnotations = injectFieldAnnotations;
    mInjectMethodAnnotations = injectMethodAnnotations;
  }

  <T> T createObject(DependencyProvider dependencyProvider, TypeToken<T> typeToken) {
    try {
      return createObjectInternal(dependencyProvider, typeToken);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new RuntimeException(e);
    }
  }

  void injectObject(DependencyProvider dependencyProvider, Object instance) {
    try {
      assignInjectableFields(dependencyProvider, instance);
      callInjectableMethods(dependencyProvider, instance);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T createObjectInternal(DependencyProvider dependencyProvider, TypeToken<T> typeToken) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<T> constructor = mConstructorSelector.chooseConstructor(typeToken);
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
          String.format("Constructor (%s)", typeToken));
    }

    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }

    T instance = constructor.newInstance(paramValues);
    injectObject(dependencyProvider, instance);
    return instance;
  }

  private Object createDependency(
      DependencyProvider dependencyProvider,
      Type paramType,
      Annotation[] paramAnnotations,
      String description) {
    TypeToken<?> paramToken = TypeToken.of(paramType);
    @Nullable Annotation qualifierAnnotation = ReflectUtil.findQualifierAnnotation(
        paramAnnotations,
        String.format("%s, Param (%s)", description, paramToken));
    DependencyKey<?> paramKey = DependencyKey.of(paramToken, qualifierAnnotation);
    return dependencyProvider.get(paramKey);
  }

  private void assignInjectableFields(DependencyProvider dependencyProvider, Object instance) throws IllegalAccessException {
    if (mInjectFieldAnnotations.isEmpty()) {
      return;
    }

    for (Field field : ReflectUtil.getAllDeclaredFields(instance.getClass())) {
      if (ReflectUtil.isAnyAnnotationPresent(field, mInjectFieldAnnotations)) {
        DependencyKey<?> paramKey = DependencyKey.fromField(field);
        Object paramValue = dependencyProvider.get(paramKey);
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        field.set(instance, paramValue);
      }
    }
  }

  private void callInjectableMethods(DependencyProvider dependencyProvider, Object instance) {
    if (mInjectMethodAnnotations.isEmpty()) {
      return;
    }

    for (Method method : ReflectUtil.getAllDeclaredMethods(instance.getClass())) {
      if (ReflectUtil.isAnyAnnotationPresent(method, mInjectMethodAnnotations)) {
        invokeMethod(method, instance, dependencyProvider);
      }
    }
  }

  private void invokeMethod(Method method, Object instance, DependencyProvider dependencyProvider) {
    int paramCount = method.getParameterCount();
    Type[] paramTypes = method.getGenericParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    Object[] paramValues = new Object[paramCount];

    for (int i = 0; i< paramCount; i++) {
      paramValues[i] = createDependency(
          dependencyProvider,
          paramTypes[i],
          paramAnnotations[i],
          String.format("Method (name: %s, during creation of: %s)", method, instance.getClass().getSimpleName()));
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
