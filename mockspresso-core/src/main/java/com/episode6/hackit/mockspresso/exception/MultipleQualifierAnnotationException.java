package com.episode6.hackit.mockspresso.exception;

import java.lang.reflect.Field;

/**
 *
 */
public class MultipleQualifierAnnotationException extends RuntimeException {

  public MultipleQualifierAnnotationException(Field field) {
    super("Multiple qualifier annotations found on field " + field.getName());
  }
}
