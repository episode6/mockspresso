package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin
import com.episode6.hackit.mockspresso.basic.plugin.injectByJavaxConfig

/**
 * Kotlin extension methods for mockspresso's Dagger plugins
 */

/**
 * Applies an [InjectionConfig] for dagger. This is the same as injectByJavaxConfig()
 * with additional support for dagger's Lazy interface.
 */
@JvmSynthetic
fun Mockspresso.Builder.injectByDaggerConfig(): Mockspresso.Builder = this
    .injectByJavaxConfig()
    .automaticLazies()

/**
 * Adds a [com.episode6.hackit.mockspresso.api.SpecialObjectMaker] to handle [dagger.Lazy]s
 * and automatically pull their dependencies from Mockspresso's dependency map.
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticLazies(): Mockspresso.Builder = specialObjectMaker(DaggerLazyMaker())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoDaggerPluginsJavaSupport {
  @JvmStatic fun injectByDaggerConfig(): MockspressoPlugin = MockspressoPlugin { it.injectByDaggerConfig() }
  @JvmStatic fun automaticLazies(): MockspressoPlugin = MockspressoPlugin { it.automaticLazies() }
}
