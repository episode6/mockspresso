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

  public RealObjectMaker(InjectionConfig injectionConfig) {
    mConstructorSelector = injectionConfig.provideConstructorSelector();
    mInjectFieldAnnotations = injectionConfig.provideInjectableFieldAnnotations();
  }

  public <T> T createObject(DependencyProvider dependencyProvider, TypeToken<T> typeToken) {
    return null;
  }
}