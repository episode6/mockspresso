package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Simple class to store mapped dependencies and type-check their fetch and retrieval.
 */
public class DependencyMap {
  private final HashMap<DependencyKey, Object> mDependencies;

  public DependencyMap() {
    mDependencies = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  public @Nullable <T, V extends T> T put(DependencyKey<T> key, V value) {
    return (T) mDependencies.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public @Nullable <T> T get(DependencyKey<T> key) {
    return (T) mDependencies.get(key);
  }
}
