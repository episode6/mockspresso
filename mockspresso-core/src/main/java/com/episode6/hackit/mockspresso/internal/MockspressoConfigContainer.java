package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;

/**
 * Class that holds the internal configuration of a mockspresso instance so that
 * we may build upon each one individually.
 */
class MockspressoConfigContainer {

  private final MockerConfig mMockerConfig;
  private final InjectionConfig mInjectionConfig;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;
  private final RealObjectMapping mRealObjectMapping;
  private final ConfigLifecycle mConfigLifecycle;

  public MockspressoConfigContainer(
      MockerConfig mockerConfig,
      InjectionConfig injectionConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker,
      RealObjectMapping realObjectMapping,
      ConfigLifecycle configLifecycle) {
    mMockerConfig = mockerConfig;
    mInjectionConfig = injectionConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
    mRealObjectMapping = realObjectMapping;
    mConfigLifecycle = configLifecycle;
  }

  MockerConfig getMockerConfig() {
    return mMockerConfig;
  }

  InjectionConfig getInjectionConfig() {
    return mInjectionConfig;
  }

  DependencyMap getDependencyMap() {
    return mDependencyMap;
  }

  SpecialObjectMaker getSpecialObjectMaker() {
    return mSpecialObjectMaker;
  }

  RealObjectMapping getRealObjectMapping() {
    return mRealObjectMapping;
  }

  void setup(MockspressoInternal instance) {
    mConfigLifecycle.setup(instance);
  }

  void teardown() {
    mConfigLifecycle.teardown();
  }
}
