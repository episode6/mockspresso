package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;

/**
 * Class that holds the internal configuration of a mockspresso instance so that
 * we may build upon each one individually.
 */
public class MockspressoConfigContainer {

  private final MockerConfig mMockerConfig;
  private final InjectionConfig mInjectionConfig;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;

  public MockspressoConfigContainer(
      MockerConfig mockerConfig,
      InjectionConfig injectionConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker) {
    mMockerConfig = mockerConfig;
    mInjectionConfig = injectionConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
  }

  public MockerConfig getMockerConfig() {
    return mMockerConfig;
  }

  public InjectionConfig getInjectionConfig() {
    return mInjectionConfig;
  }

  public DependencyMap getDependencyMap() {
    return mDependencyMap;
  }

  public SpecialObjectMaker getSpecialObjectMaker() {
    return mSpecialObjectMaker;
  }
}
