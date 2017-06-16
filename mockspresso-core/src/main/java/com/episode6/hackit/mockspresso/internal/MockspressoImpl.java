package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.inject.Provider;

/**
 * future implementation of mockspresso functionality
 */
class MockspressoImpl implements Mockspresso, MockspressoInternal {

  private final MockspressoConfigContainer mMockspressoConfigContainer;
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final RealObjectMaker mRealObjectMaker;
  private final Provider<MockspressoBuilderImpl> mBuilderProvider;

  MockspressoImpl(
      MockspressoConfigContainer mockspressoConfigContainer,
      DependencyProviderFactory dependencyProviderFactory,
      RealObjectMaker realObjectMaker,
      Provider<MockspressoBuilderImpl> builderProvider) {
    mMockspressoConfigContainer = mockspressoConfigContainer;
    mDependencyProviderFactory = dependencyProviderFactory;
    mRealObjectMaker = realObjectMaker;
    mBuilderProvider = builderProvider;
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
  public void inject(Object instance) {
    DependencyProvider dependencyProvider = mDependencyProviderFactory.getDependencyProviderFor(
        DependencyKey.of(instance.getClass()));
    mRealObjectMaker.injectObject(
        dependencyProvider,
        instance);
  }

  @Override
  public Builder buildUpon() {
    MockspressoBuilderImpl builder = mBuilderProvider.get();
    builder.setParent(mMockspressoConfigContainer);
    return builder;
  }

  @Override
  public MockspressoConfigContainer getConfig() {
    return mMockspressoConfigContainer;
  }
}
