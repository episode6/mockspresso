package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 *
 */
public class QuickBuildMockspresso {

  public static QuickMockspressoBuilder with() {
    return com.episode6.hackit.mockspresso.quick.internal.QuickMockspressoBuilder.create();
  }

  public static QuickMockspressoBuilder buildUpon(Mockspresso mockspresso) {
    return com.episode6.hackit.mockspresso.quick.internal.QuickMockspressoBuilder.buildUpon(mockspresso);
  }
}
