package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * Utility class to create new {@link Mockspresso.Builder}s
 */
public class MockspressoBuilder {
  public static Mockspresso.Builder create() {
    return MockspressoBuilderImpl.PROVIDER.get();
  }
}
