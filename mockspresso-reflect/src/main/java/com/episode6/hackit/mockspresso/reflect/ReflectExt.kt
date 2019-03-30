package com.episode6.hackit.mockspresso.reflect

/**
 * Kotlin extensions for reflection utils using reified type parameters
 */

/**
 * Creates a [TypeToken] for [T]
 */
inline fun <reified T : Any> typeToken(): TypeToken<T> = object : TypeToken<T>() {}

/**
 * Creates a [DependencyKey] for [T] with the provided qualifier annotation
 */
inline fun <reified T : Any> dependencyKey(qualifier: Annotation? = null): DependencyKey<T> =
    DependencyKey.of<T>(typeToken<T>(), qualifier)
