package com.episode6.hackit.mockspresso.easymock.powermock;

import org.fest.assertions.core.Condition;

/**
 * conditions to check for mocks
 */
public class Conditions {

  public static <T> Condition<T> mockCondition() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return value.toString().startsWith("EasyMock");
      }
    };
  }
}
