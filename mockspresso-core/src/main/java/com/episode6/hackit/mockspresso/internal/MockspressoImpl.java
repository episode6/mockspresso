package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements Mockspresso {
  @Override
  public <T> T constructRealObject(Class<T> clazz) {
    return null;
  }

  @Override
  public <T> T constructRealObject(TypeToken<T> typeToken) {
    return null;
  }
}
