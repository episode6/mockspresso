package com.episode6.hackit.mockspresso

import com.episode6.hackit.mockspresso.api.ObjectProvider
import com.episode6.hackit.mockspresso.reflect.dependencyKey
import com.episode6.hackit.mockspresso.reflect.typeToken

/**
 * Kotlin extensions to mockspresso's api
 */

/**
 * Apply a specific instance of an object as a mockspresso dependency.
 * Kotlin alias for [Mockspresso.Builder.dependencyProvider].
 *
 * @param qualifier Optional qualifier annotation that applies to the binding key of this dependency
 * @param value Kotlin function/lambda that returns the dependency
 * @return the builder
 */
inline fun <reified T : Any> Mockspresso.Builder.dependencyOf(
    qualifier: Annotation? = null,
    noinline value: ()->T?
): Mockspresso.Builder = dependencyProvider(dependencyKey<T>(qualifier), ObjectProvider<T>(value))

/**
 * Instruct mockspresso to create a real object (of type [IMPL]) to be bound using a dependency key of type [BIND].
 * Kotlin alias for [Mockspresso.Builder.realObject]
 *
 * @param qualifier Optional qualifier annotation that applies to the binding key of this dependency
 * @return the builder
 */
inline fun <reified BIND : Any, reified IMPL : BIND> Mockspresso.Builder.realImplOf(
    qualifier: Annotation? = null
): Mockspresso.Builder = realObject(dependencyKey<BIND>(qualifier), typeToken<IMPL>())

/**
 * Instruct mockspresso to create a real object and bind with a dependencyKey of its own concrete type
 * Convenience method for [realImplOf] when both BIND and IMPL are the same type
 *
 * @param qualifier Optional qualifier annotation that applies to the binding key of this dependency
 * @return the builder
 */
inline fun <reified BIND_AND_IMPL : Any> Mockspresso.Builder.realClassOf(
    qualifier: Annotation? = null
): Mockspresso.Builder = realImplOf<BIND_AND_IMPL, BIND_AND_IMPL>(qualifier)
