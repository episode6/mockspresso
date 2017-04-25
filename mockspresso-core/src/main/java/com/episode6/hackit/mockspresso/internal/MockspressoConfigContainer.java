package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoInitializer;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;

import java.util.LinkedList;
import java.util.List;

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
  private final List<MockspressoInitializer> mInitializers;

  public MockspressoConfigContainer(
      MockerConfig mockerConfig,
      InjectionConfig injectionConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker,
      RealObjectMapping realObjectMapping,
      List<MockspressoInitializer> initializers) {
    mMockerConfig = mockerConfig;
    mInjectionConfig = injectionConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
    mRealObjectMapping = realObjectMapping;
    mInitializers = new LinkedList<>(initializers);
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

  public List<MockspressoInitializer> getInitializers() {
    List<MockspressoInitializer> initializers = new LinkedList<>();
    synchronized (mInitializers) {
      initializers.addAll(mInitializers);
    }
    return initializers;
  }

  public void executeAndClearInitializers(Mockspresso instance) {
    // we must clear our initializer list before executing them
    // to ensure they are able to build upon this mockspresso instance
    List<MockspressoInitializer> initializers = new LinkedList<>();
    synchronized (mInitializers) {
      initializers.addAll(mInitializers);
      mInitializers.clear();
    }
    for (MockspressoInitializer initializer : initializers) {
      initializer.setup(instance);
    }
  }

  public MockspressoBuilderImpl newBuilder() {
    MockspressoBuilderImpl builder = new MockspressoBuilderImpl();
    builder.setParent(this);
    return builder;
  }
}
