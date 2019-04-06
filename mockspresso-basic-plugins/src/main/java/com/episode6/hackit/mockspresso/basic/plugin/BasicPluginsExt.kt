package com.episode6.hackit.mockspresso.basic.plugin

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin

/**
 * Kotlin extensions for mockspresso's basic plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.InjectionConfig] for simple creation of objects via
 * their shortest constructor.
 */
@JvmSynthetic
fun Mockspresso.Builder.injectBySimpleConfig(): Mockspresso.Builder = plugin(SimpleInjectMockspressoPlugin())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.InjectionConfig] for javax.inject based object creation
 * (looks for constructors, fields and methods annotated with @Inject).
 * Also includes special object support for [javax.inject.Provider]s
 */
@JvmSynthetic
fun Mockspresso.Builder.injectByJavaxConfig(): Mockspresso.Builder = plugin(JavaxInjectMockspressoPlugin())
