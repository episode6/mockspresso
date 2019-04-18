@file:Suppress("DEPRECATION")

package com.episode6.hackit.mockspresso.mockito.powermock

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin

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
fun Mockspresso.Builder.mockByPowerMockito(): Mockspresso.Builder = mocker(PowerMockitoConfig())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + Mockito
 * AND applies a PowerMockRule as an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
 * Requires your project have the same dependencies as [mockByPowerMockito]
 * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMockitoRule(): Mockspresso.Builder = this
    .mockByPowerMockito()
    .outerRule(org.powermock.modules.junit4.rule.PowerMockRule())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoPowerMockitoPluginsJavaSupport {
  @JvmStatic fun mockByPowerMockito(): MockspressoPlugin = MockspressoPlugin { it.mockByPowerMockito() }
  @JvmStatic fun mockByPowerMockitoRule(): MockspressoPlugin = MockspressoPlugin { it.mockByPowerMockitoRule() }
}
