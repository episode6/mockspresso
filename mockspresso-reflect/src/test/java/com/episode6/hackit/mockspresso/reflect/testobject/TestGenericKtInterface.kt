package com.episode6.hackit.mockspresso.reflect.testobject

/**
 * A test interface that uses the `out` keyword on its type variable.
 * Used to display a java/kotlin interop issue when this interface is
 * misused in java code.
 */
interface TestGenericKtInterface<out T> {
  fun getSomething(): T
}
