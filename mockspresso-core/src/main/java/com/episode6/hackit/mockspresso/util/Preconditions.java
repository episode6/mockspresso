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
}
