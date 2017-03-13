package com.episode6.hackit.mockspresso;

import org.fest.assertions.core.Condition;
import org.mockito.Mockito;

/**
 * some conditions we use.
 */
public class Conditions {

  public static <T> Condition<T> mockitoMock() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return Mockito.mockingDetails(value).isMock();
      }
    };
  }

  public static <T> Condition<T> mockitoSpy() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return Mockito.mockingDetails(value).isSpy();
      }
    };
  }

  public static <T> Condition<T> easyMockMock() {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return value.toString().startsWith("EasyMock");
      }
    };
  }

  public static <T> Condition<T> rawClass(final Class<?> clazz) {
    return new Condition<T>() {
      @Override
      public boolean matches(T value) {
        return value.getClass() == clazz;
      }
    };
  }
}
