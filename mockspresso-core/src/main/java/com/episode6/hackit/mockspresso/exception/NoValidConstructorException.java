package com.episode6.hackit.mockspresso.exception;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * Exception thrown when trying to create a real object that
 * has no constructor.
 */
public class NoValidConstructorException extends NullPointerException {

  public NoValidConstructorException(TypeToken<?> typeToken) {
    super("No valid constructor found for " + typeToken.toString());
  }
}
