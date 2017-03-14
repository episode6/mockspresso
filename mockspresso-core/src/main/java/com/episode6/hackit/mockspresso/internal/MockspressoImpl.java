package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements Mockspresso, MockspressoInternal {

  private final MockspressoConfigContainer mMockspressoConfigContainer;
  private final DependencyProvider mDependencyProvider;
  private final RealObjectMaker mRealObjectMaker;

  MockspressoImpl(
      MockspressoConfigContainer mockspressoConfigContainer,
      DependencyProvider dependencyProvider,
      RealObjectMaker realObjectMaker) {
    mMockspressoConfigContainer = mockspressoConfigContainer;
    mDependencyProvider = dependencyProvider;
    mRealObjectMaker = realObjectMaker;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return create(TypeToken.of(clazz));
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return mRealObjectMaker.createObject(mDependencyProvider, typeToken);
  }

  @Override
  public Builder buildUpon() {
    MockspressoBuilderImpl builder = new MockspressoBuilderImpl();
    builder.setParent(getConfig());
    return builder;
  }

  @Override
  public MockspressoConfigContainer getConfig() {
    return mMockspressoConfigContainer;
  }
}
