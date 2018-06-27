package com.episode6.hackit.mockspresso.basic.plugin.javax;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * Exception thrown when trying to create an object with multiple @Inject constructors
 * (when using the JavaxInjectionConfig
 */
public class MultipleInjectConstructorException extends VerifyError {
  public MultipleInjectConstructorException(TypeToken<?> objectType) {
    super("Multiple constructors annotated with @Inject on type: " + objectType.toString());
  }
}
