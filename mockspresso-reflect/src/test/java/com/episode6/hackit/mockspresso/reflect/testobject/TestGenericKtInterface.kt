package com.episode6.hackit.mockspresso.reflect.testobject

/**
 *
 */
interface TestGenericKtInterface<out T> {
  fun getSomething(): T
}
