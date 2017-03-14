package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;

/**
 * An abstract class to handle the logic of a "delayed" mockspresso instance (usually
 * when using a @Rule).
 */
public abstract class AbstractDelayedMockspresso implements Mockspresso {

  private static final String ERROR_MESSAGE = "Called Mockspresso.create() before delegate was created.";
  private @Nullable Mockspresso mDelegate = null;

  protected synchronized void setDelegate(@Nullable Mockspresso delegate) {
    mDelegate = delegate;
  }

  @Override
  public synchronized <T> T create(Class<T> clazz) {
    return Preconditions.assertNotNull(mDelegate, ERROR_MESSAGE).create(clazz);
  }

  @Override
  public synchronized <T> T create(TypeToken<T> typeToken) {
    return Preconditions.assertNotNull(mDelegate, ERROR_MESSAGE).create(typeToken);
  }

  @Override
  public synchronized Builder buildUpon() {
    return Preconditions.assertNotNull(mDelegate, ERROR_MESSAGE).buildUpon();
  }
}
