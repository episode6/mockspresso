package com.episode6.hackit.mockspresso.guava

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin

/**
 * Kotlin extension methods for mockspresso's Guava plugins
 */

/**
 * Applies a [com.episode6.hackit.mockspresso.api.SpecialObjectMaker] to provide real implementations
 * of guava's [com.google.common.util.concurrent.ListenableFuture]s that return dependencies from mockspresso
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticListenableFutures(): Mockspresso.Builder = specialObjectMaker(ListenableFutureMaker())

/**
 * Applies a [com.episode6.hackit.mockspresso.api.SpecialObjectMaker] to provide real implementations
 * of guava's [com.google.common.base.Supplier]s that return dependencies from mockspresso
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticSuppliers(): Mockspresso.Builder = specialObjectMaker(SupplierMaker())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoGuavaPluginsJavaSupport {
  @JvmStatic fun automaticListenableFutures(): MockspressoPlugin = MockspressoPlugin { it.automaticListenableFutures() }
  @JvmStatic fun automaticSuppliers(): MockspressoPlugin = MockspressoPlugin { it.automaticSuppliers() }
}
