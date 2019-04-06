package com.episode6.hackit.mockspresso.mockito.powermock

import com.episode6.hackit.mockspresso.Mockspresso

/**
 * Kotlin extension methods for mockspresso's Powermock + Mockito plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + Mockito.
 * Requires your project have a dependency on org.mockito:mockito-core v2.x,
 * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
 * Also requires your test be runWith the [org.powermock.modules.junit4.PowerMockRunner]
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMockito(): Mockspresso.Builder = plugin(PowerMockitoPlugin())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + Mockito
 * AND applies a PowerMockRule as an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
 * Requires your project have the same dependencies as [mockByPowerMockito]
 * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMockitoRule(): Mockspresso.Builder = plugin(PowerMockitoRulePlugin())
