package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;

/**
 *
 */
public class BuildQuickMockspresso {

  public static QuickMockspresso.Builder with() {
    return new QuickMockspressoImpl.Builder(BuildMockspresso.with());
  }

  public static QuickMockspresso.Builder buildUpon(Mockspresso mockspresso) {
    return new QuickMockspressoImpl.Builder(mockspresso.buildUpon());
  }
}
