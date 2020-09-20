package com.episode6.hackit.mockspresso.guava

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.genericParameterKey
import com.episode6.hackit.mockspresso.reflect.isParameterizedGeneric
import com.google.common.base.Supplier
import com.google.common.base.Suppliers

/**
 * Special object handling for guava's [Supplier]
 */
internal class SupplierMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean = key.isParameterizedGeneric(Supplier::class)

  @Suppress("UNCHECKED_CAST")
  override fun <T> makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<T>): T? {
    if (!canMakeObject(key)) return null
    return Suppliers.ofInstance(dependencyProvider[key.genericParameterKey()!!]) as T
  }
}
