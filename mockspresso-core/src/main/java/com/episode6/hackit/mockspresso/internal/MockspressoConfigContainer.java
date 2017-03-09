package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;

/**
 * Class that holds the internal configuration of a mockspresso instance.
 * We retain the config instances that are used so that we may
 * buildUpon them individually.
 */
public class MockspressoConfigContainer {

  private final MockerConfig mMockerConfig;
  private final InjectionConfig mInjectionConfig;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;

  private final DependencyProvider mDependencyProvider;
  private final RealObjectMaker mRealObjectMaker;

  public MockspressoConfigContainer(
      MockerConfig mockerConfig,
      InjectionConfig injectionConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker) {
    mMockerConfig = mockerConfig;
    mInjectionConfig = injectionConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;

    mDependencyProvider = new DependencyProviderImpl(
        mockerConfig.provideMockMaker(),
        dependencyMap,
        specialObjectMaker);
    mRealObjectMaker = new RealObjectMaker(
        mInjectionConfig);
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

  public DependencyProvider getDependencyProvider() {
    return mDependencyProvider;
  }

  public RealObjectMaker getRealObjectMaker() {
    return mRealObjectMaker;
  }
}
