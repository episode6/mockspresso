package com.episode6.hackit.mockspresso.easymock

import com.episode6.hackit.mockspresso.Mockspresso
import org.easymock.EasyMock.*
import org.easymock.EasyMockSupport
import org.easymock.Mock
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Tests [com.episode6.hackit.mockspresso.easymock.EasyMockPluginsExtKt]
 */
class EasyMockPluginsExtTest {

  @Mock
  lateinit var builder: Mockspresso.Builder

  @Before fun setup() {
    EasyMockSupport.injectMocks(this)
  }

  @Test fun testEasyMockExtensionSourceOfTruth() {
    expect(builder.mocker(anyObject(EasyMockMockerConfig::class.java))).andReturn(builder)
    replay(builder)

    val result = builder.mockByEasyMock()

    verify(builder)
    assertThat(result)
        .isNotNull
        .isEqualTo(builder)
  }
}
