package com.episode6.hackit.mockspresso.testing;

import org.fest.assertions.core.Condition;

/**
 *
 */
public class Conditions {
  public static <T> Condition<T> rawClass(final Class<?> clazz) {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return value.getClass() == clazz;
      }
    };
  }
}
