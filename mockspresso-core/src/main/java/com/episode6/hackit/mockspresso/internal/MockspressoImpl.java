package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements Mockspresso {
  @Override
  public <T> T create(Class<T> clazz) {
    return null;
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return null;
  }

  @Override
  public Builder buildUpon() {
    return new MockspressoBuilderImpl().parent(this);
  }
}