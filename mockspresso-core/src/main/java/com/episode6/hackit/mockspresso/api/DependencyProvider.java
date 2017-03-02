package com.episode6.hackit.mockspresso.api;

/**
 * A interface for a class that can provide dependencies for an object.
 */
public interface DependencyProvider {
  <T> T get(DependencyKey<T> key);
}
