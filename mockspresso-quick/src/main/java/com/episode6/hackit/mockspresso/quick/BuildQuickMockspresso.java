package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * Entry-point to create new instances of {@link QuickMockspresso.Builder}
 */
public class BuildQuickMockspresso {

  /**
   * @return a brand new instance of {@link QuickMockspresso.Builder}
   */
  public static QuickMockspresso.Builder with() {
    return new QuickMockspressoImpl.Builder(BuildMockspresso.with());
  }

  /**
   * Builds upon an existing mockspresso instance with a new {@link QuickMockspresso.Builder}
   * @param mockspresso The {@link Mockspresso} instance to wrap
   * @return and new {@link QuickMockspresso.Builder} built upon the supplied mockspresso instance.
   */
  public static QuickMockspresso.Builder buildUpon(Mockspresso mockspresso) {
    return new QuickMockspressoImpl.Builder(mockspresso.buildUpon());
  }
}
