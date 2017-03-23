package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains a set of DependencyKeys to treat as real objects, mapped to their implementation
 * classes and whether they should be mapped into the dependencyMap (so they are not created
 * multiple times per test)
 */
public class RealObjectMapping {

  private @Nullable RealObjectMapping mParentMap = null;
  private final Map<DependencyKey, Implementation> mMap = new HashMap<>();

  void setParentMap(RealObjectMapping parentMap) {
    mParentMap = parentMap;
  }

  public <T> void put(DependencyKey<T> key, TypeToken<? extends T> implementationToken, boolean shouldMapDependency) {
    if (mMap.containsKey(key)) {
      throw new RepeatedDependencyDefinedException(key);
    }
    mMap.put(key, new Implementation(implementationToken, shouldMapDependency));
  }

  public boolean containsKey(DependencyKey key) {
    return mMap.containsKey(key) || (mParentMap != null && mParentMap.containsKey(key));
  }

  @SuppressWarnings("unchecked")
  public <T, V extends T> TypeToken<V> getImplementation(DependencyKey<T> key) {
    if (mMap.containsKey(key)) {
      return (TypeToken<V>) mMap.get(key).mImplementationToken;
    }
    return mParentMap == null ? null : (TypeToken<V>) mParentMap.getImplementation(key);
  }

  public boolean shouldMapDependency(DependencyKey key) {
    if (mMap.containsKey(key)) {
      return mMap.get(key).mShouldMapDependency;
    }
    return mParentMap != null && mParentMap.shouldMapDependency(key);
  }

  private static class Implementation {
    private final TypeToken<?> mImplementationToken;
    private final boolean mShouldMapDependency;

    Implementation(TypeToken<?> implementationToken, boolean shouldMapDependency) {
      mImplementationToken = implementationToken;
      mShouldMapDependency = shouldMapDependency;
    }
  }
}
