package com.episode6.hackit.mockspresso.plugin.javax;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.exception.MultipleInjectConstructorException;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of {@link InjectionConfig} designed for the javax.inject package.
 * With this config, we look for the constructor annotated with @Inject, and prefer it.
 * If no @Inject constructor is found, we will use and empty constructor, if one is found.
 * If multiple @Inject constructors are found, an exception will be thrown.
 */
public class JavaxInjectionConfig implements InjectionConfig {
  private static final List<Class<? extends Annotation>> INJECT_ANNOTATION = Collections.<Class<? extends Annotation>>singletonList(Inject.class);

  private final JavaxConstructorSelector mConstructorSelector = new JavaxConstructorSelector();

  @Override
  public ConstructorSelector provideConstructorSelector() {
    return mConstructorSelector;
  }

  @Override
  public List<Class<? extends Annotation>> provideInjectableFieldAnnotations() {
    return INJECT_ANNOTATION;
  }

  @Override
  public List<Class<? extends Annotation>> provideInjectableMethodAnnotations() {
    return INJECT_ANNOTATION;
  }

  private static class JavaxConstructorSelector implements ConstructorSelector {

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> Constructor<T> chooseConstructor(TypeToken<T> typeToken) {
      Constructor<T> injectConstructor = null;
      Constructor<T> emptyConstructor = null;

      for (Constructor<?> constructor : typeToken.getRawType().getDeclaredConstructors()) {
        if (constructor.isAnnotationPresent(Inject.class)) {
          if (injectConstructor != null) {
            throw new MultipleInjectConstructorException(typeToken);
          }
          injectConstructor = (Constructor<T>) constructor;
          continue;
        }

        if (constructor.getParameterCount() == 0) {
          emptyConstructor = (Constructor<T>) constructor;
        }
      }

      if (injectConstructor != null) {
        return injectConstructor;
      }
      return emptyConstructor;
    }
  }
}
