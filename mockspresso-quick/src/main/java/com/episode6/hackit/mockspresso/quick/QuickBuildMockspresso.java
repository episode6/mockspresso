package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.quick.internal.QuickMockspressoBuilderImpl;

/**
 *
 */
public class QuickBuildMockspresso {

  public static QuickMockspressoBuilder with() {
    return new QuickMockspressoBuilderImpl(BuildMockspresso.with());
  }

  public static QuickMockspressoBuilder buildUpon(Mockspresso mockspresso) {
    return new QuickMockspressoBuilderImpl(mockspresso.buildUpon());
  }
}
