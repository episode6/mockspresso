package com.episode6.hackit.mockspresso.extend.testext;

import com.episode6.hackit.mockspresso.extend.MockspressoExtension;

/**
 * Very simple mockspresso extension test definition. Adds two methods to the builder.
 */
public interface TestMockspresso extends MockspressoExtension<TestMockspresso.Builder> {

  interface Rule extends MockspressoExtension.Rule<TestMockspresso.Builder> {}

  interface Builder extends MockspressoExtension.Builder<TestMockspresso, TestMockspresso.Rule, TestMockspresso.Builder> {

    TestMockspresso.Builder simpleInjector();
    TestMockspresso.Builder mockWithMockito();
  }
}
