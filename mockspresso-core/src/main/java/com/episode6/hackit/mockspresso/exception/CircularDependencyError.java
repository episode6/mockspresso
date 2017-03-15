package com.episode6.hackit.mockspresso.exception;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.util.List;

/**
 * Error thrown when trying to declare multiple @RealObjects that
 * create a circular dependency.
 */
public class CircularDependencyError extends VerifyError {

  public CircularDependencyError() {
    super("Attempted to create circular dependency of @RealObjects: [TODO!]");
  }
}
