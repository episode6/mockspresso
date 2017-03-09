package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * A configuration interface that defines how we construct real objects.
 */
public interface InjectionConfig {

  interface ConstructorSelector {
    <T> Constructor<T> chooseConstructor(TypeToken<T> typeToken);
  }

  ConstructorSelector provideConstructorSelector();
  List<Class<? extends Annotation>> provideInjectableFieldAnnotations();
}
