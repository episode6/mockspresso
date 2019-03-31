package com.episode6.hackit.mockspresso

import com.episode6.hackit.mockspresso.api.ObjectProvider
import com.episode6.hackit.mockspresso.reflect.dependencyKey

/**
 * Kotlin extensions to mockspresso's api
 */

/**
 * Apply a specific instance of an object as a mockspresso dependency.
 * Kotlin alias for [Mockspresso.Builder.dependencyProvider].
 *
 * @param qualifier Optional qualifier annotation that applies to the binding key of this dependency
 * @param value Kotlin function/lambda that returns the dependency
 */
inline fun <reified T : Any> Mockspresso.Builder.dependencyOf(
    qualifier: Annotation? = null,
    noinline value: ()->T?
): Mockspresso.Builder = dependencyProvider(dependencyKey<T>(qualifier), ObjectProvider<T>(value))
