package com.episode6.hackit.mockspresso

import com.episode6.hackit.mockspresso.internal.createMockspressoBuilder

/**
 * Contains a static method to create new [Mockspresso.Builder]s
 */
object BuildMockspresso {

  /**
   * @return a new [Mockspresso.Builder].
   */
  @JvmStatic
  fun with(): Mockspresso.Builder = createMockspressoBuilder()
}
