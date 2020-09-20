package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.TypeToken
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import dagger.Lazy
import java.lang.reflect.ParameterizedType

/**
 * An implementation of SpecialObjectMaker for [dagger.Lazy].
 */
internal class DaggerLazyMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean =
      key.typeToken.rawType == Lazy::class.java &&
          key.typeToken.type is ParameterizedType

  @Suppress("UNCHECKED_CAST")
  override fun <T> makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? {
    if (!canMakeObject(key)) return null
    return Lazy { dependencyProvider[key.genericParameterKey()!!] } as T
  }
}
