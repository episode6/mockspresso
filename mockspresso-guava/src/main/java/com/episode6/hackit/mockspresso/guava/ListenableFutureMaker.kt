package com.episode6.hackit.mockspresso.guava

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import com.episode6.hackit.mockspresso.reflect.isParameterizedGeneric
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

/**
 * Special object maker for guava's [ListenableFuture]. Uses param type
 * and ident annotation to look up actual dependency, and returns an immediate
 * future.
 */
internal class ListenableFutureMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean = key.isParameterizedGeneric(ListenableFuture::class)

  @Suppress("UNCHECKED_CAST")
  override fun <T> makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? {
    if (!canMakeObject(key)) return null
    return Futures.immediateFuture(dependencyProvider[key.genericParameterKey()!!]) as T
  }
}
