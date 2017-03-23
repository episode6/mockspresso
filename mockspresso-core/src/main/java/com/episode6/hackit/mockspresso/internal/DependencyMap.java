package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

/**
 * Simple class to store mapped dependencies and type-check their fetch and retrieval.
 */
public class DependencyMap {
  private @Nullable DependencyMap mParentMap = null;
  private final HashMap<DependencyKey, InstanceContainer> mDependencies = new HashMap<>();

  public void setParentMap(DependencyMap parentMap) {
    mParentMap = parentMap;
  }

  @SuppressWarnings("unchecked")
  public <T, V extends T> void put(
      DependencyKey<T> key,
      V value,
      @Nullable DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      throw new RepeatedDependencyDefinedException(key);
    }
    mDependencies.put(key, new InstanceContainer(value, dependencyValidator));
  }

  @SuppressWarnings("unchecked")
  public @Nullable <T> T get(DependencyKey<T> key, DependencyValidator dependencyValidator) {
    if (mDependencies.containsKey(key)) {
      InstanceContainer container = mDependencies.get(key);
      dependencyValidator.append(container.dependencyValidator);
      return (T) container.object;
    }
    return mParentMap == null ? null : mParentMap.get(key, dependencyValidator);
  }

  public boolean containsKey(DependencyKey key) {
    return mDependencies.containsKey(key) || (mParentMap != null && mParentMap.containsKey(key));
  }

  public void assertDoesNotContainAny(Collection<DependencyKey> newKeys) {
    for (DependencyKey key : newKeys) {
      if (containsKey(key)) {
        throw new RepeatedDependencyDefinedException(key);
      }
    }
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
