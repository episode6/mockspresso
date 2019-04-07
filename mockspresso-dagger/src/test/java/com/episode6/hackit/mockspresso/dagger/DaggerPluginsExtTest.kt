package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.stubbing.Answer

/**
 * Tests [com.episode6.hackit.mockspresso.dagger.DaggerPluginsExtKt]
 */
class DaggerPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test
  fun testSimplePluginSourceOfTruth() {
    val result = builder.injectByDaggerConfig()

    assertThat(result).isEqualTo(builder)
    verify(builder).plugin(any<DaggerMockspressoPlugin>())
  }
}
