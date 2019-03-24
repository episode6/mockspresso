package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * A configuration interface that defines how we construct real objects.
 */
public interface InjectionConfig {

  interface ConstructorSelector {
    @Nullable <T> Constructor<T> chooseConstructor(@NotNull TypeToken<T> typeToken);
  }

  @NotNull ConstructorSelector provideConstructorSelector();
  @NotNull List<Class<? extends Annotation>> provideInjectableFieldAnnotations();
  @NotNull List<Class<? extends Annotation>> provideInjectableMethodAnnotations();
}
