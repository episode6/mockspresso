package com.episode6.hackit.mockspresso.easymock.powermock

import com.episode6.hackit.mockspresso.Mockspresso

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
fun Mockspresso.Builder.mockByPowerMock(): Mockspresso.Builder = plugin(EasyPowerMockPlugin())

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] for Powermock + EasyMock AND
 * applies a PowerMockRule as an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
 * Requires your project have the same dependencies as [mockByPowerMock]
 * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByPowerMockRule(): Mockspresso.Builder = plugin(EasyPowerMockRulePlugin())