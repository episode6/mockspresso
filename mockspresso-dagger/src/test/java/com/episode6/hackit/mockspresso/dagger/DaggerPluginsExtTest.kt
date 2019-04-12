package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.basic.plugin.injectByJavaxConfig
import io.mockk.*
import org.fest.assertions.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 * Tests [com.episode6.hackit.mockspresso.dagger.DaggerPluginsExtKt]
 */
class DaggerPluginsExtTest {

  lateinit var builder: Mockspresso.Builder

  @Before fun setup() {
    mockkStatic("com.episode6.hackit.mockspresso.basic.plugin.BasicPluginsExtKt")
    builder = mockk<Mockspresso.Builder>(relaxed = true).apply {
      every { injector(any()) } returns this
      every { specialObjectMaker(any()) } returns this
    }
    every { mockk<Mockspresso.Builder>().injectByJavaxConfig() } returns builder
  }

  @After fun teardown() {
    unmockkAll()
  }

  @Test
  fun testSimplePluginSourceOfTruth() {
    val slot = slot<SpecialObjectMaker>()

    val result = builder.injectByDaggerConfig()

    assertThat(result).isEqualTo(builder)
    verify { builder.injectByJavaxConfig() }
    verify { builder.specialObjectMaker(capture(slot)) }
    assertThat(slot.captured).isInstanceOf(DaggerLazyMaker::class.java)
  }

  @Test fun testAutomaticLaziesSourceOfTruth() {
    val slot = slot<SpecialObjectMaker>()

    val result = builder.automaticLazies()

    assertThat(result).isEqualTo(builder)
    verify { builder.specialObjectMaker(capture(slot)) }
    assertThat(slot.captured).isInstanceOf(DaggerLazyMaker::class.java)
  }
}
