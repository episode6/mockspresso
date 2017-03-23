package com.episode6.hackit.mockspresso.exception;

/**
 * Exception thrown when looking for a Qualifier annotation on a field or parameter
 */
public class MultipleQualifierAnnotationException extends VerifyError {
  public MultipleQualifierAnnotationException(String description) {
    super("Multiple qualifier annotations found: " + description);
  }
}
