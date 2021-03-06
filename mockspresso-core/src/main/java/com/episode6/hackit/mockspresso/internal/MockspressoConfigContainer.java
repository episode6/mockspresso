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
  private final ResourcesLifecycleManager mResourcesLifecycleManager;

  public MockspressoConfigContainer(
      MockerConfig mockerConfig,
      InjectionConfig injectionConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker,
      RealObjectMapping realObjectMapping,
      ResourcesLifecycleManager resourcesLifecycleManager) {
    mMockerConfig = mockerConfig;
    mInjectionConfig = injectionConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
    mRealObjectMapping = realObjectMapping;
    mResourcesLifecycleManager = resourcesLifecycleManager;
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
    mResourcesLifecycleManager.setup(instance);
  }

  void teardown() {
    mResourcesLifecycleManager.teardown();
  }
}
