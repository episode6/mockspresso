package com.episode6.hackit.mockspresso.api

import com.episode6.hackit.mockspresso.Mockspresso

/**
 * An interface that can encapsulate multiple related mockspresso components into a single plugin.
 */
interface MockspressoPlugin {
  fun apply(builder: Mockspresso.Builder): Mockspresso.Builder
}