package com.episode6.hackit.mockspresso.exception;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * Exception thrown when we find multiple (null) @RealObjects, mapped to the same DependencyKey, but using
 * different implementations (defined on the @RealObject annotation)
 */
public class RealObjectMappingMismatchException extends RuntimeException {
  public RealObjectMappingMismatchException(DependencyKey<?> dependencyKey) {
    super("Multiple real objects with different implementations mapped to key: " + dependencyKey.toString());
  }
}
