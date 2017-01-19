package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {
  <T> T constructRealObject(Class<T> clazz);
  <T> T constructRealObject(TypeToken<T> typeToken);
}
