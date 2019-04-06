package com.episode6.hackit.mockspresso.easymock

import com.episode6.hackit.mockspresso.Mockspresso

/**
 * Kotlin extension methods for mockspresso's EasyMock plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] to support EasyMock
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByEasyMock(): Mockspresso.Builder = plugin(EasyMockPlugin())
