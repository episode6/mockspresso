@file:Suppress("DEPRECATION")

package com.episode6.hackit.mockspresso.easymock.powermock

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin

/**
 * Kotlin extension methods for mockspresso's Powermock + EasyMock plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + EasyMock.
 * Requires your project have a dependency on org.easymock:easymock v3.4,
 * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
 * Also requires your test be runWith the [org.powermock.modules.junit4.PowerMockRunner]
 * <p>
 * WARNING, the @org.easymock.Mock annotation may not work correctly when using Mockspresso +
 * easymock + PowerMockRunner, as easymock overwrites Mockspresso's annotated mocks at the last minute.
 * To work around this problem, use powermock's @Mock, @MockNice and @MockStrict annotations instead.
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMock(): Mockspresso.Builder = mocker(EasyPowerMockMockerConfig())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + EasyMock AND
 * applies a PowerMockRule as an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
 * Requires your project have the same dependencies as [mockByPowerMock]
 * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMockRule(): Mockspresso.Builder = this
    .mockByPowerMock()
    .outerRule(org.powermock.modules.junit4.rule.PowerMockRule())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoEasyPowerMockPluginsJavaSupport {
  @JvmStatic fun mockByPowerMock(): MockspressoPlugin = MockspressoPlugin { it.mockByPowerMock() }
  @JvmStatic fun mockByPowerMockRule(): MockspressoPlugin = MockspressoPlugin { it.mockByPowerMockRule() }
}
