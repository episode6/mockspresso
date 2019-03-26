package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.internal.MockspressoBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Contains a static method to create new {@link Mockspresso.Builder}s
 */
public class BuildMockspresso {

  /**
   * @return a new {@link Mockspresso.Builder}.
   */
  public static @NotNull Mockspresso.Builder with() {
    return MockspressoBuilder.create();
  }
}
