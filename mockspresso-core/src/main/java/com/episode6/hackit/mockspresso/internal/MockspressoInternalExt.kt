package com.episode6.hackit.mockspresso.internal

import com.episode6.hackit.mockspresso.Mockspresso

/**
 * Internal utility function to create new [Mockspresso.Builder]s
 */
internal fun createMockspressoBuilder(): Mockspresso.Builder = MockspressoBuilderImpl.PROVIDER.get()
