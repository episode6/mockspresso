package com.episode6.hackit.mockspresso.easymock.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.annotation.RealObject
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.easymock.Conditions.mockCondition
import com.episode6.hackit.mockspresso.easymock.mockByEasyMock
import com.episode6.hackit.mockspresso.testing.matches
import org.easymock.Mock
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Named

/**
 * Test mocking using kotlin ext methods
 */
class EasyMockKotlinMockingTest {

  private interface TestDep1
  private interface TestDep2
  private interface TestDep3
  private class TestObj(
      val dep1: TestDep1,
      @Named("dep2") val dep2: TestDep2,
      val dep3: TestDep3)

  @get:Rule val mockspresso = BuildMockspresso.with()
      .injectBySimpleConfig()
      .mockByEasyMock()
      .buildRule()

  @Mock private lateinit var dep1: TestDep1
  @Mock @field:Named("dep2") private lateinit var dep2: TestDep2

  @RealObject private lateinit var testObj: TestObj

  @Test fun assertDepsAreMocks() {
    assertThat(testObj.dep1).isEqualTo(dep1).matches(mockCondition())
    assertThat(testObj.dep2).isEqualTo(dep2).matches(mockCondition())
    assertThat(testObj.dep3).matches(mockCondition())
  }
}
