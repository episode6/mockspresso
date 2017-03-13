package com.episode6.hackit.mockspresso.mockito;

import org.fest.assertions.core.Condition;
import org.mockito.Mockito;

/**
 * conditions to check for mocks and spies
 */
public class Conditions {

  public static <T> Condition<T> mockCondition() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return Mockito.mockingDetails(value).isMock();
      }
    };
  }

  public static <T> Condition<T> spyCondition() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return Mockito.mockingDetails(value).isSpy();
      }
    };
  }
}
