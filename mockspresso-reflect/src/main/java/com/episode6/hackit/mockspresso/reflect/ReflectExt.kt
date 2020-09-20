package com.episode6.hackit.mockspresso.reflect

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Kotlin extensions for reflection utils using reified type parameters
 */

/**
 * Creates a TypeToken for [T]
 */
inline fun <reified T : Any> typeToken(): TypeToken<T> = object : TypeToken<T>() {}

/**
 * Creates a [DependencyKey] for [T] with the provided qualifier annotation
 *
 * @param qualifier Optional qualifier annotation that applies to this key
 */
inline fun <reified T : Any> dependencyKey(qualifier: Annotation? = null): DependencyKey<T> =
    DependencyKey.of<T>(typeToken<T>(), qualifier)

/**
 * return true if the receiver [DependencyKey] represents a parameterized generic of [clazz].
 * If [clazz] is not generic this method should always return false.
 */
fun DependencyKey<*>.isParameterizedGeneric(clazz: KClass<*>): Boolean =
    typeToken.rawType == clazz.java && typeToken.type is ParameterizedType

/**
 * If the receiver [DependencyKey] represents a parameterized type, this method returns
 * a new dependency key for the first type argument with the same identity annotation.
 *
 * I.e. if the receiver represents `@Named Provider<String>`, this method will return a
 * dependency key representing `@Named String`
 */
@Suppress("UNCHECKED_CAST")
fun DependencyKey<*>.genericParameterKey(): DependencyKey<Any>? {
  val paramType = (typeToken.type as? ParameterizedType)?.actualTypeArguments?.get(0) ?: return null
  return DependencyKey.of(TypeToken.of(paramType), identityAnnotation) as DependencyKey<Any>
}
