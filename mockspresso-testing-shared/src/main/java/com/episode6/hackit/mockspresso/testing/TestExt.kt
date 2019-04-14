package com.episode6.hackit.mockspresso.testing

import org.fest.assertions.api.ObjectAssert
import org.fest.assertions.core.Condition

/**
 * Some extension methods to make testing with fest less verbose.
 * I'd like to switch it up with assertJ but who has that kind of time.
 */

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> ObjectAssert<*>.isConcreteInstanceOf(): ObjectAssert<T> =
    isExactlyInstanceOf(T::class.java) as ObjectAssert<T>

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> ObjectAssert<*>.isCastableAs(): ObjectAssert<T> =
    isInstanceOf(T::class.java) as ObjectAssert<T>

fun <T : Any> ObjectAssert<T>.matches(condition: Condition<T>): ObjectAssert<T> = `is`(condition)

fun <T : Any> ObjectAssert<T>.satisfies(block: (T) -> Unit): ObjectAssert<T> = matches(object : Condition<T>() {
  override fun matches(value: T): Boolean {
    block(value)
    return true
  }
})

fun <T: Any> className(simpleName: String) = object : Condition<T>("expected class named $simpleName") {
  override fun matches(value: T?): Boolean = value!!.javaClass.simpleName == simpleName
}
