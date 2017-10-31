package com.episode6.hackit.mockspresso.api;

/**
 * Similar to {@link javax.inject.Provider} but get method is allowed to throw
 */
public interface ObjectProvider<V> {
  V get() throws Throwable;
}
