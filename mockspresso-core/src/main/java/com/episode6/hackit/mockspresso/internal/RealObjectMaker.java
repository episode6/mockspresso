package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Does the job of actually creating real objects
 */
public class RealObjectMaker  {
  private final InjectionConfig.ConstructorSelector mConstructorSelector;
  private final List<Class<? extends Annotation>> mInjectFieldAnnotations;
  private final DependencyProvider mDependencyProvider;

  public RealObjectMaker(
      InjectionConfig injectionConfig,
      DependencyProvider dependencyProvider) {
    mConstructorSelector = injectionConfig.provideConstructorSelector();
    mInjectFieldAnnotations = injectionConfig.provideInjectableFieldAnnotations();
    mDependencyProvider = dependencyProvider;
  }

  public <T> T createObject(TypeToken<T> typeToken) {
    return null;
  }
}
