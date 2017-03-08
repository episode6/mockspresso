package com.episode6.hackit.mockspresso.util;

/**
 * Some preconditions
 */
public class Preconditions {
  public static void assertNull(Object object, String message) {
    if (object != null) {
      throw new IllegalArgumentException(message);
    }
  }

  public static <T> T assertNotNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
    return object;
  }
}
