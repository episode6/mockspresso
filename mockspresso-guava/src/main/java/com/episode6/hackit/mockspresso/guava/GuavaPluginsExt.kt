package com.episode6.hackit.mockspresso.guava

import com.episode6.hackit.mockspresso.Mockspresso

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
