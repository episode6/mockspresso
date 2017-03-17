package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements Mockspresso, MockspressoInternal {

  private final MockspressoConfigContainer mMockspressoConfigContainer;
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final RealObjectMaker mRealObjectMaker;

  MockspressoImpl(
      MockspressoConfigContainer mockspressoConfigContainer,
      DependencyProviderFactory dependencyProviderFactory,
      RealObjectMaker realObjectMaker) {
    mMockspressoConfigContainer = mockspressoConfigContainer;
    mDependencyProviderFactory = dependencyProviderFactory;
    mRealObjectMaker = realObjectMaker;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return create(TypeToken.of(clazz));
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    DependencyProvider dependencyProvider = mDependencyProviderFactory.getDependencyProviderFor(
        DependencyKey.of(typeToken));
    return mRealObjectMaker.createObject(
        dependencyProvider,
        typeToken);
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
