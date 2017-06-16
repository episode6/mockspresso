package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract class to handle the logic of a "delayed" mockspresso instance (usually
 * when using a @Rule).
 */
abstract class AbstractDelayedMockspresso implements Mockspresso, MockspressoInternal {

  private static final String ERROR_MESSAGE = "Called Mockspresso.create() before delegate was created.";
  private @Nullable MockspressoInternal mDelegate = null;
  private final Set<DelayedMockspressoBuilder> mDelayedBuilders = new HashSet<>();

  private final Provider<MockspressoBuilderImpl> mBuilderProvider;

  protected AbstractDelayedMockspresso(Provider<MockspressoBuilderImpl> builderProvider) {
    mBuilderProvider = builderProvider;
  }

  synchronized void setDelegate(@Nullable MockspressoInternal delegate) {
    if (mDelegate != null) {
      passParentToDelayedBuilders(null);
      mDelegate.getConfig().teardown();
    }

    mDelegate = delegate;

    if (delegate != null) {
      MockspressoConfigContainer newConfig = delegate.getConfig();
      newConfig.setup(delegate);
      passParentToDelayedBuilders(newConfig);
    }
  }

  private void passParentToDelayedBuilders(@Nullable MockspressoConfigContainer parentConfig) {
    for (DelayedMockspressoBuilder delayedBuilder : mDelayedBuilders) {
      delayedBuilder.setParent(parentConfig);
    }
  }

  private synchronized MockspressoInternal getDelegate() {
    return Preconditions.assertNotNull(mDelegate, ERROR_MESSAGE);
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return getDelegate().create(clazz);
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return getDelegate().create(typeToken);
  }

  @Override
  public void inject(Object instance) {
    getDelegate().inject(instance);
  }

  @Override
  public synchronized Builder buildUpon() {
    if (mDelegate != null) {
      return mDelegate.buildUpon();
    }

    DelayedMockspressoBuilder delayedBuilder = new DelayedMockspressoBuilder(mBuilderProvider);
    mDelayedBuilders.add(delayedBuilder);
    return delayedBuilder;
  }

  @Override
  public MockspressoConfigContainer getConfig() {
    return getDelegate().getConfig();
  }

  protected MockspressoBuilderImpl newBuilder() {
    return mBuilderProvider.get();
  }
}
