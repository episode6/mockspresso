package com.episode6.hackit.mockspresso.basic.plugin.javax

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import com.episode6.hackit.mockspresso.reflect.isParameterizedGeneric
import javax.inject.Provider

/**
 * An implementation of SpecialObjectMaker for [javax.inject.Provider].
 */
internal class ProviderMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean = key.isParameterizedGeneric(Provider::class)
  override fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any? {
    if (!canMakeObject(key)) return null
    return Provider { dependencyProvider[key.genericParameterKey()!!] }
  }
}
