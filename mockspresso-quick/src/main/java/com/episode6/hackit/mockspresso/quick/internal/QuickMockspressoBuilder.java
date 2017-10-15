package com.episode6.hackit.mockspresso.quick.internal;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * Utility class to create new {@link com.episode6.hackit.mockspresso.quick.QuickMockspressoBuilder}s
 */
public class QuickMockspressoBuilder {
  public static com.episode6.hackit.mockspresso.quick.QuickMockspressoBuilder create() {
    return new QuickMockspressoBuilderImpl(BuildMockspresso.with());
  }

  public static com.episode6.hackit.mockspresso.quick.QuickMockspressoBuilder buildUpon(Mockspresso mockspresso) {
    return new QuickMockspressoBuilderImpl(mockspresso.buildUpon());
  }
}
