package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test


/**
 * Tests [com.episode6.hackit.mockspresso.dagger.DaggerPluginsExtKt]
 */
class DaggerPluginsExtTest {

  val builder = mockk<Mockspresso.Builder>().apply {
    every { plugin(any()) } returns this
    every { specialObjectMaker(any()) } returns this
  }

  @Test
  fun testSimplePluginSourceOfTruth() {
    val slot = slot<MockspressoPlugin>()

    val result = builder.injectByDaggerConfig()

    assertThat(result).isEqualTo(builder)
    verify { builder.plugin(capture(slot)) }
    assertThat(slot.captured).isInstanceOf(DaggerMockspressoPlugin::class.java)
  }

  @Test fun testAutomaticLaziesSourceOfTruth() {
    val slot = slot<SpecialObjectMaker>()

    val result = builder.automaticLazies()

    assertThat(result).isEqualTo(builder)
    verify { builder.specialObjectMaker(capture(slot)) }
    assertThat(slot.captured).isInstanceOf(DaggerLazyMaker::class.java)
  }
}
