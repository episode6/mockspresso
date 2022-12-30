package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import com.episode6.hackit.mockspresso.reflect.isParameterizedGeneric
import dagger.Lazy

/**
 * An implementation of SpecialObjectMaker for [dagger.Lazy].
 */
internal class DaggerLazyMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean = key.isParameterizedGeneric(Lazy::class)
  override fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any? {
    if (!canMakeObject(key)) return null
    return Lazy { dependencyProvider[key.genericParameterKey()!!] }
  }
}