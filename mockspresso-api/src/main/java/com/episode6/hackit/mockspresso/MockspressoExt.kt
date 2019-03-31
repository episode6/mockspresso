package com.episode6.hackit.mockspresso

import com.episode6.hackit.mockspresso.api.ObjectProvider
import com.episode6.hackit.mockspresso.reflect.dependencyKey

/**
 * Kotlin extensions to mockspresso's api
 */

inline fun <reified T : Any> Mockspresso.Builder.dependencyOf(qualifier: Annotation? = null, noinline valueProvider: ()->T?): Mockspresso.Builder =
    dependencyProvider(dependencyKey<T>(qualifier), ObjectProvider<T>(valueProvider))
