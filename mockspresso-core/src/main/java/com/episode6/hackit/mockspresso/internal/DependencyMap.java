package com.episode6.hackit.mockspresso.internal;

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
  private final HashMap<DependencyKey, InstanceContainer> mDependencies = new HashMap<>();

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
  @Nullable <T> T get(DependencyKey<T> key, DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      InstanceContainer container = mDependencies.get(key);
      dependencyValidator.append(container.dependencyValidator);
      return (T) container.object;
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

  private static class InstanceContainer {
    private final Object object;
    private final DependencyValidator dependencyValidator;

    InstanceContainer(Object object, DependencyValidator dependencyValidator) {
      this.object = object;
      this.dependencyValidator = dependencyValidator;
    }
  }
}
