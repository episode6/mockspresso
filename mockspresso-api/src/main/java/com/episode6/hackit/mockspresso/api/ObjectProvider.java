package com.episode6.hackit.mockspresso.api;

import org.jetbrains.annotations.Nullable;

/**
 * Similar to {@link javax.inject.Provider} but get method is allowed to throw
 */
public interface ObjectProvider<V> {
  @Nullable V get() throws Throwable;
}
