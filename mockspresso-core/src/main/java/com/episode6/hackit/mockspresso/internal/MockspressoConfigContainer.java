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

  public RealObjectMapping getRealObjectMapping() {
    return mRealObjectMapping;
  }

  public MockspressoBuilderImpl newBuilder() {
    MockspressoBuilderImpl builder = MockspressoBuilderImpl.PROVIDER.get();
    builder.setParent(this);
    return builder;
  }

  public void setup(MockspressoInternal instance) {
    mConfigLifecycle.setup(instance);
  }

  public void teardown() {
    mConfigLifecycle.teardown();
  }
}
