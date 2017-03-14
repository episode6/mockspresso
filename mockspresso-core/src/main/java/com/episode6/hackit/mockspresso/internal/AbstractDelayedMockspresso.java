package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract class to handle the logic of a "delayed" mockspresso instance (usually
 * when using a @Rule).
 */
public abstract class AbstractDelayedMockspresso implements Mockspresso, MockspressoInternal {

  private static final String ERROR_MESSAGE = "Called Mockspresso.create() before delegate was created.";
  private @Nullable MockspressoInternal mDelegate = null;
  private final Set<DelayedMockspressoBuilder> mChildBuilders = new HashSet<>();

  protected synchronized void setDelegate(@Nullable MockspressoInternal delegate) {
    mDelegate = delegate;
    MockspressoConfigContainer parentConfig = delegate == null ? null : delegate.getConfig();
    for (DelayedMockspressoBuilder childBuilder : mChildBuilders) {
      childBuilder.setParent(parentConfig);
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
  public synchronized Builder buildUpon() {
    if (mDelegate != null) {
      return mDelegate.buildUpon();
    }

    DelayedMockspressoBuilder delayedBuilder = new DelayedMockspressoBuilder();
    mChildBuilders.add(delayedBuilder);
    return delayedBuilder;
  }

  @Override
  public MockspressoConfigContainer getConfig() {
    return getDelegate().getConfig();
  }
}
