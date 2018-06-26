package com.episode6.hackit.mockspresso.extend.testext;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;

/**
 *
 */
public class BuildTestMockspresso {

  public static TestMockspresso.Builder with() {
    return new TestMockspressoImpl.Builder(BuildMockspresso.with());
  }

  // methods below are only for unit testing the AbstractMockspressoExtension methods, and are not meant as an example

  public static TestMockspresso wrapMockspresso(Mockspresso mockspresso) {
    return new TestMockspressoImpl(mockspresso);
  }

  public static TestMockspresso.Builder wrapBuilder(Mockspresso.Builder builder) {
    return new TestMockspressoImpl.Builder(builder);
  }

  public static TestMockspresso.Rule wrapRule(Mockspresso.Rule rule) {
    return new TestMockspressoImpl.Rule(rule);
  }
}
