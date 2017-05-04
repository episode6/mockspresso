package com.episode6.hackit.mockspresso;

/**
 * Contains a static method to create new {@link Mockspresso.Builder}s
 */
public class BuildMockspresso {

  /**
   * @return a new {@link Mockspresso.Builder}.
   */
  public static Mockspresso.Builder with() {
    return com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl.PROVIDER.get();
  }
}
