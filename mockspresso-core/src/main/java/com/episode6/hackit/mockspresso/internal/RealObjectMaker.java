package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.exception.NoValidConstructorException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Does the job of actually creating real objects
 */
public class RealObjectMaker  {
  private final InjectionConfig.ConstructorSelector mConstructorSelector;
  private final List<Class<? extends Annotation>> mInjectFieldAnnotations;

  public RealObjectMaker(
      InjectionConfig.ConstructorSelector constructorSelector,
      List<Class<? extends Annotation>> injectFieldAnnotations) {
    mConstructorSelector = constructorSelector;
    mInjectFieldAnnotations = injectFieldAnnotations;
  }

  public <T> T createObject(DependencyProvider dependencyProvider, TypeToken<T> typeToken) {
    try {
      return createObjectInternal(dependencyProvider, typeToken);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
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
      TypeToken<?> paramToken = TypeToken.of(paramTypes[i]);
      @Nullable Annotation qualifierAnnotation = ReflectUtil.findQualifierAnnotation(
          paramAnnotations[i],
          String.format("Constructor (%s), Param (%s)", typeToken, paramToken));
      DependencyKey<?> paramKey = new DependencyKey<>(paramToken, qualifierAnnotation);
      paramValues[i] = dependencyProvider.get(paramKey);
    }

    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }

    T instance = constructor.newInstance(paramValues);
    postProcessNewObject(dependencyProvider, instance);
    return instance;
  }

  private void postProcessNewObject(DependencyProvider dependencyProvider, Object instance) throws IllegalAccessException {
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
}
