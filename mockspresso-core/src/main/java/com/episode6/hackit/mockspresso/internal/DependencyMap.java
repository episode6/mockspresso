package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Simple class to store mapped dependencies and type-check their fetch and retrieval.
 */
public class DependencyMap {
  private final @Nullable DependencyMap mParentMap;
  private final HashMap<DependencyKey, Object> mDependencies;

  public DependencyMap(@Nullable DependencyMap parentMap) {
    mParentMap = parentMap;
    mDependencies = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  public <T, V extends T> boolean put(DependencyKey<T> key, V value) {
    if (!mDependencies.containsKey(key)) {
      mDependencies.put(key, value);
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public @Nullable <T> T get(DependencyKey<T> key) {
    if (mDependencies.containsKey(key)) {
      return (T) mDependencies.get(key);
    }
    return mParentMap == null ? null : mParentMap.get(key);
  }

  public boolean containsKey(DependencyKey key) {
    return mDependencies.containsKey(key) || (mParentMap != null && mParentMap.containsKey(key));
  }

  public DependencyMapImporter importFrom() {
    return new DependencyMapImporter(this);
  }
}
