package com.episode6.hackit.mockspresso.basic.plugin.javax

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import java.lang.reflect.ParameterizedType
import javax.inject.Provider

/**
 * An implementation of SpecialObjectMaker for [javax.inject.Provider].
 */
internal class ProviderMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean =
      key.typeToken.rawType == Provider::class.java &&
          key.typeToken.type is ParameterizedType

  @Suppress("UNCHECKED_CAST")
  override fun <T> makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? {
    if (!canMakeObject(key)) return null
    return Provider { dependencyProvider[key.genericParameterKey()!!] } as T
  }
}
