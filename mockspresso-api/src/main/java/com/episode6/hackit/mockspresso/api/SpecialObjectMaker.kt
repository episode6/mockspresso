package com.episode6.hackit.mockspresso.api

import com.episode6.hackit.mockspresso.reflect.DependencyKey

/**
 * A class that creates "special" objects, i.e. objects that, by default, should not
 * be simply mocked. An example of this is [javax.inject.Provider], where we'd
 * rather return a real instance that provides a mock (or a mapped dependency).
 */
interface SpecialObjectMaker {
  fun canMakeObject(key: DependencyKey<*>): Boolean
  fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any?
}
