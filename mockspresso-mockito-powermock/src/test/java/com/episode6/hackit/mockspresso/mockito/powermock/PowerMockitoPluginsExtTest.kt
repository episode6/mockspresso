package com.episode6.hackit.mockspresso.mockito.powermock

import com.episode6.hackit.mockspresso.Mockspresso
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.stubbing.Answer

/**
 * Tests [com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPluginsExtKt]
 */
class PowerMockitoPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test
  fun testPowerMockitoPluginSourceOfTruth() {
    builder.mockByPowerMockito()

    verify(builder).plugin(any<PowerMockitoPlugin>())
  }

  @Test
  fun testPowerMockitoRulePluginSourceOfTruth() {
    builder.mockByPowerMockitoRule()

    verify(builder).plugin(any<PowerMockitoRulePlugin>())
  }
}
