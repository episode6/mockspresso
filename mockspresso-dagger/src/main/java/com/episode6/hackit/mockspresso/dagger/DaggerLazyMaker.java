package com.episode6.hackit.mockspresso.dagger;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import dagger.Lazy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * An implementation of SpecialObjectMaker for {@link dagger.Lazy}.
 */
public class DaggerLazyMaker implements SpecialObjectMaker {

  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    return key.typeToken.getRawType() == Lazy.class &&
        key.typeToken.getType() instanceof ParameterizedType;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key) {
    if (!canMakeObject(key)) {
      return null;
    }

    Type paramType = ((ParameterizedType)key.typeToken.getType()).getActualTypeArguments()[0];
    DependencyKey<?> subKey = DependencyKey.of(TypeToken.of(paramType), key.identityAnnotation);
    return (T) new DependencyInstanceLazy(dependencyProvider, subKey);
  }

  private static class DependencyInstanceLazy<V> implements Lazy<V> {
    private final DependencyProvider mDependencyProvider;
    private final DependencyKey<V> mDependencyKey;

    private DependencyInstanceLazy(
        DependencyProvider dependencyProvider,
        DependencyKey<V> dependencyKey) {
      mDependencyProvider = dependencyProvider;
      mDependencyKey = dependencyKey;
    }

    @Override
    public V get() {
      return mDependencyProvider.get(mDependencyKey);
    }
  }
}
