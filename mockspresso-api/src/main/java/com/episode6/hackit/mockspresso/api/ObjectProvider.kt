package com.episode6.hackit.mockspresso.api

/**
 * Similar to [javax.inject.Provider] but get method is allowed to throw
 */
interface ObjectProvider<V> {
  @Throws(Throwable::class)
  fun get(): V?
}
