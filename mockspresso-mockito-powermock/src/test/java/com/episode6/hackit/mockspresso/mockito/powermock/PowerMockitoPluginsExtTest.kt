package com.episode6.hackit.mockspresso.mockito.powermock

import com.episode6.hackit.mockspresso.Mockspresso
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.stubbing.Answer
import org.powermock.modules.junit4.rule.PowerMockRule

/**
 * Tests [com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPluginsExtKt]
 */
class PowerMockitoPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test
  fun testPowerMockitoPluginSourceOfTruth() {
    val result = builder.mockByPowerMockito()

    assertThat(result).isEqualTo(builder)
    verify(builder).mocker(any<PowerMockitoConfig>())
  }

  @Test
  fun testPowerMockitoRulePluginSourceOfTruth() {
    val result = builder.mockByPowerMockitoRule()

    assertThat(result).isEqualTo(builder)
    verify(builder).mocker(any<PowerMockitoConfig>())
    verify(builder).outerRule(any<PowerMockRule>())
  }
}
