package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.internal.MockspressoBuilder;

/**
 * Contains a static method to create new {@link Mockspresso.Builder}s
 */
public class BuildMockspresso {

  /**
   * @return a new {@link Mockspresso.Builder}.
   */
  public static Mockspresso.Builder with() {
    return MockspressoBuilder.create();
  }
}
