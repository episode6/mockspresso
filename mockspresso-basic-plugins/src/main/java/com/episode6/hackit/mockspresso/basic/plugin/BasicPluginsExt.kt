package com.episode6.hackit.mockspresso.basic.plugin

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectionConfig
import com.episode6.hackit.mockspresso.basic.plugin.javax.ProviderMaker
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectionConfig

/**
 * Kotlin extensions for mockspresso's basic plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.InjectionConfig] for simple creation of objects via
 * their shortest constructor.
 */
@JvmSynthetic
fun Mockspresso.Builder.injectBySimpleConfig(): Mockspresso.Builder = injector(SimpleInjectionConfig())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.InjectionConfig] for javax.inject based object creation
 * (looks for constructors, fields and methods annotated with @Inject).
 * Also includes special object support for [javax.inject.Provider]s
 */
@JvmSynthetic
fun Mockspresso.Builder.injectByJavaxConfig(): Mockspresso.Builder = this
    .injector(JavaxInjectionConfig())
    .automaticProviders()

/**
 * Adds a [com.episode6.hackit.mockspresso.api.SpecialObjectMaker] to handle [javax.inject.Provider]s
 * and automatically pull their dependencies from Mockspresso's dependency map.
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticProviders(): Mockspresso.Builder = specialObjectMaker(ProviderMaker())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoBasicPluginsJavaSupport {
  @JvmStatic fun injectBySimpleConfig(): MockspressoPlugin = MockspressoPlugin { it.injectBySimpleConfig() }
  @JvmStatic fun injectByJavaxConfig(): MockspressoPlugin = MockspressoPlugin { it.injectByJavaxConfig() }
  @JvmStatic fun automaticProviders(): MockspressoPlugin = MockspressoPlugin { it.automaticProviders() }
}
