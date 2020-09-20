package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.mockito.mockByMockito
import com.nhaarman.mockitokotlin2.mock
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LifecycleTest {

  @Test fun testLifecycleOnTheFly() {
    val testRes = LifecycleTestResources()

    with(testRes) {
      assertThat(isSetup).isFalse
      assertThat(isTornDown).isFalse
    }

    val mockspresso = BuildMockspresso.with()
        .injectBySimpleConfig()
        .mockByMockito()
        .testResources(testRes)
        .build()

    with(testRes) {
      assertThat(isSetup).isTrue
      assertThat(isTornDown).isFalse
    }

    mockspresso.teardown()

    with(testRes) {
      assertThat(isSetup).isTrue
      assertThat(isTornDown).isTrue
    }
  }

  @Test fun testRuleLifecycle() {
    val testRes = LifecycleTestResources()
    val mockspresso = BuildMockspresso.with()
        .injectBySimpleConfig()
        .mockByMockito()
        .testResources(testRes)
        .buildRule()

    with(testRes) {
      assertThat(isSetup).isFalse
      assertThat(isTornDown).isFalse
    }

    mockspresso.apply(mock(), mock(), mock()).evaluate()

    with(testRes) {
      assertThat(isSetup).isTrue
      assertThat(isTornDown).isTrue
    }
  }
}
