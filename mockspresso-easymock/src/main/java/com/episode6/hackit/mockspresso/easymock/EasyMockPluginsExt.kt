package com.episode6.hackit.mockspresso.easymock

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin

/**
 * Kotlin extension methods for mockspresso's EasyMock plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] to support EasyMock
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByEasyMock(): Mockspresso.Builder = mocker(EasyMockMockerConfig())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoEasyMockPluginsJavaSupport {
  @JvmStatic fun mockByEasyMock(): MockspressoPlugin = MockspressoPlugin { it.mockByEasyMock() }
}
