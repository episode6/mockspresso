package com.episode6.hackit.mockspresso.extend.testext;

import com.episode6.hackit.mockspresso.BuildMockspresso;

/**
 *
 */
public class BuildTestMockspresso {

  public static TestMockspresso.Builder with() {
    return new TestMockspressoImpl.Builder(BuildMockspresso.with());
  }
}
