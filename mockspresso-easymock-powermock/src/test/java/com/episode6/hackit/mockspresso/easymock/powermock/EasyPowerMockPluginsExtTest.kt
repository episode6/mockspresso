@file:Suppress("DEPRECATION")

package com.episode6.hackit.mockspresso.easymock.powermock

import com.episode6.hackit.mockspresso.Mockspresso
import org.easymock.EasyMock.*
import org.easymock.EasyMockSupport
import org.easymock.Mock
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.powermock.modules.junit4.rule.PowerMockRule

/**
 * Tests [com.episode6.hackit.mockspresso.easymock.EasyMockPluginsExtKt]
 */
class EasyPowerMockPluginsExtTest {

  @Mock
  lateinit var builder: Mockspresso.Builder

  @Before fun setup() {
    EasyMockSupport.injectMocks(this)
  }

  @Test fun testPowerEasyMockExtensionSourceOfTruth() {
    expect(builder.mocker(anyObject(EasyPowerMockMockerConfig::class.java))).andReturn(builder)
    replay(builder)

    val result = builder.mockByPowerMock()

    verify(builder)
    assertThat(result)
        .isNotNull
        .isEqualTo(builder)
  }

  @Test fun testPowerEasyMockRuleExtensionSourceOfTruth() {
    expect(builder.mocker(anyObject(EasyPowerMockMockerConfig::class.java))).andReturn(builder)
    expect(builder.outerRule(anyObject(PowerMockRule::class.java))).andReturn(builder)
    replay(builder)

    val result = builder.mockByPowerMockRule()

    verify(builder)
    assertThat(result)
        .isNotNull
        .isEqualTo(builder)
  }
}
