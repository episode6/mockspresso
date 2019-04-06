package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso

/**
 * Kotlin extension methods for mockspresso's Dagger plugins
 */

/**
 * Applies an [InjectionConfig] for dagger. This is the same as injectByJavaxConfig()
 * with additional support for dagger's Lazy interface.
 */
@JvmSynthetic
fun Mockspresso.Builder.injectByDaggerConfig(): Mockspresso.Builder = plugin(DaggerMockspressoPlugin())
