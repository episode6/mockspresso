package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.mockito.mockByMockito
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.api.Assertions.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LifecycleRuleTest {

  private val testRes = LifecycleTestResources()

  @get:Rule val mockspresso = BuildMockspresso.with()
      .injectBySimpleConfig()
      .mockByMockito()
      .testResources(testRes)
      .buildRule()

  @Test fun testLifecycleRule() {

    with(testRes) {
      assertThat(isSetup).isTrue
      assertThat(isTornDown).isFalse
    }
  }

  @Test fun testTeardownException() {
    try {
      mockspresso.teardown()
    } catch (e: IllegalArgumentException) {
      // expected
      return
    }
    fail("Expected an IllegalArgumentException")
  }

}
