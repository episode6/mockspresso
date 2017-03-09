package com.episode6.hackit.mockspresso.exception;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * Exception thrown when trying multiple fields/objects are bound to the same
 * {@link DependencyKey} in a single Mockspresso instance.
 */
public class RepeatedDependencyDefinedException extends RuntimeException {

  public RepeatedDependencyDefinedException(DependencyKey key) {
    super("Dependency defined multiple times in same Mockspresso instance: " + key.toString());
  }
}
