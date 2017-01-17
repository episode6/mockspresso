package com.episode6.hackit.mockspresso;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {
  <T> T constructRealObject(Class<T> clazz);
  <T> T constructRealObject(TypeToken<T> typeToken);
}
