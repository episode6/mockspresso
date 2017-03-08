package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements MockspressoInternal {

  private final MockerConfig mMockerConfig;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;

  MockspressoImpl(
      MockerConfig mockerConfig,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker) {
    mMockerConfig = mockerConfig;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return create(TypeToken.of(clazz));
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return null;
  }

  @Override
  public Builder buildUpon() {
    return new MockspressoBuilderImpl(this);
  }

  @Override
  public MockerConfig getMockerConfig() {
    return mMockerConfig;
  }

  @Override
  public DependencyMap getDependencyMap() {
    return mDependencyMap;
  }

  @Override
  public SpecialObjectMaker getSpecialObjectMaker() {
    return mSpecialObjectMaker;
  }

}
