package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * A interface for a class that can provide dependencies for an object.
 */
public interface DependencyProvider {
  <T> T get(DependencyKey<T> key);
}
