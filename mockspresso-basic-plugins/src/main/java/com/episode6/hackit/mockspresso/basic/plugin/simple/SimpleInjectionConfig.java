package com.episode6.hackit.mockspresso.basic.plugin.simple;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

/**
 * A very simple implementation of InjectionConfig. Chooses the constructor with the least number of arguments,
 * and provides no injectable field annotations.
 */
public class SimpleInjectionConfig implements InjectionConfig {

  private final ConstructorSelector mConstructorSelector = new SimpleConstructorSelector();

  @Override
  public ConstructorSelector provideConstructorSelector() {
    return mConstructorSelector;
  }

  @Override
  public List<Class<? extends Annotation>> provideInjectableFieldAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public List<Class<? extends Annotation>> provideInjectableMethodAnnotations() {
    return Collections.emptyList();
  }

  private static class SimpleConstructorSelector implements ConstructorSelector {

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable <T> Constructor<T> chooseConstructor(TypeToken<T> typeToken) {
      Constructor<T> found = null;
      for (Constructor<?> constructor : typeToken.getRawType().getDeclaredConstructors()) {
        if (found == null || constructor.getParameterCount() < found.getParameterCount()) {
          found = (Constructor<T>) constructor;
        }
      }
      return found;
    }
  }
}
