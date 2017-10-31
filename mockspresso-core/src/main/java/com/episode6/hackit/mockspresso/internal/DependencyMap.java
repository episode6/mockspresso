package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.ObjectProvider;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

/**
 * Simple class to store mapped dependencies and type-check their fetch and retrieval.
 */
class DependencyMap {
  private @Nullable DependencyMap mParentMap = null;
  private final HashMap<DependencyKey, Container> mDependencies = new HashMap<>();

  DependencyMap() {}

  DependencyMap deepCopy() {
    DependencyMap newMap = new DependencyMap();
    newMap.setParentMap(mParentMap);
    newMap.mDependencies.putAll(mDependencies);
    return newMap;
  }

  void setParentMap(DependencyMap parentMap) {
    mParentMap = parentMap;
  }

  @SuppressWarnings("unchecked")
  <T, V extends T> void put(
      DependencyKey<T> key,
      V value,
      @Nullable DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      throw new RepeatedDependencyDefinedException(key);
    }
    mDependencies.put(key, new InstanceContainer(value, dependencyValidator));
  }

  @SuppressWarnings("unchecked")
  <T, V extends T> void putProvider(
      DependencyKey<T> key,
      ObjectProvider<V> value,
      @Nullable DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      throw new RepeatedDependencyDefinedException(key);
    }
    mDependencies.put(key, new ProviderContainer(value, dependencyValidator));
  }

  @SuppressWarnings("unchecked")
  @Nullable <T> T get(DependencyKey<T> key, DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      Container container = mDependencies.get(key);
      dependencyValidator.append(container.getValidator());
      return (T) container.getObject();
    }
    return mParentMap == null ? null : mParentMap.get(key, dependencyValidator);
  }

  boolean containsKey(DependencyKey key) {
    return mDependencies.containsKey(key) || (mParentMap != null && mParentMap.containsKey(key));
  }

  Set<DependencyKey> keySet() {
    return mDependencies.keySet();
  }

  /**
   * Only clears this dependency map, parents remain untouched
   */
  void clear() {
    mDependencies.clear();
  }

  private interface Container {
    Object getObject();
    DependencyValidator getValidator();
  }

  private static class InstanceContainer implements Container {
    private final Object object;
    private final DependencyValidator dependencyValidator;

    InstanceContainer(Object object, DependencyValidator dependencyValidator) {
      this.object = object;
      this.dependencyValidator = dependencyValidator;
    }

    @Override
    public Object getObject() {
      return object;
    }

    @Override
    public DependencyValidator getValidator() {
      return dependencyValidator;
    }
  }

  private static class ProviderContainer implements Container {
    private final ObjectProvider objectProvider;
    private final DependencyValidator dependencyValidator;

    private Object object;

    ProviderContainer(ObjectProvider objectProvider, DependencyValidator dependencyValidator) {
      this.objectProvider = objectProvider;
      this.dependencyValidator = dependencyValidator;
      this.object = null;
    }

    @Override
    public Object getObject() {
      if (object == null) {
        try {
          object = objectProvider.get();
        } catch (Throwable throwable) {
          throw new RuntimeException(throwable);
        }
      }
      return object;
    }

    @Override
    public DependencyValidator getValidator() {
      return dependencyValidator;
    }
  }
}
