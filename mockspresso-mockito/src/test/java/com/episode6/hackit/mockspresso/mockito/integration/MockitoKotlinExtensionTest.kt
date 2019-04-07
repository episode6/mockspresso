package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.annotation.Dependency
import com.episode6.hackit.mockspresso.annotation.RealObject
import com.episode6.hackit.mockspresso.basic.plugin.injectByJavaxConfig
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.createNew
import com.episode6.hackit.mockspresso.getDependencyOf
import com.episode6.hackit.mockspresso.injectType
import com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition
import com.episode6.hackit.mockspresso.mockito.mockByMockito
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Testing kotlin extensions to [com.episode6.hackit.mockspresso.Mockspresso] with mockito
 */
class MockitoKotlinExtensionTest {

  private interface TestDependencyInterface
  private class TestDependency : TestDependencyInterface

  private class TestObject(val dep: TestDependencyInterface)

  private class TestGeneric<T : Any> @Inject constructor() {
    @Inject lateinit var dep: T
  }

  private class InjectTestResources {
    @RealObject lateinit var testGeneric: TestGeneric<TestDependencyInterface>
  }

  @get:Rule val mockspresso = BuildMockspresso.with()
      .injectBySimpleConfig()
      .mockByMockito()
      .buildRule()

  @Dependency private val testDependency: TestDependencyInterface = TestDependency()

  @RealObject private lateinit var testObject: TestObject


  @Test fun testCreateConcrete() {
    val testObject2: TestObject = mockspresso.createNew()

    assertThat(testObject2)
        .isNotNull
        .isNot(mockCondition())
        .isNotEqualTo(testObject)
    assertThat(testObject2.dep)
        .isEqualTo(testObject.dep)
        .isEqualTo(testDependency)
  }

  @Test fun testCreateGeneric() {
    InjectTestResources().apply {
      val testObject2: TestGeneric<TestDependencyInterface> = mockspresso.buildUpon()
          .injectByJavaxConfig()
          .testResources(this)
          .build()
          .createNew()

      assertThat(testObject2)
          .isNotNull
          .isNot(mockCondition())
          .isNotEqualTo(testGeneric)
      assertThat(testObject2.dep)
          .isEqualTo(testGeneric.dep)
          .isEqualTo(testDependency)
    }
  }

  @Test fun testGetConcreteObject() {
    val testObject2: TestObject = mockspresso.getDependencyOf()!!

    assertThat(testObject2)
        .isNotNull
        .isNot(mockCondition())
        .isEqualTo(testObject)
  }

  @Test fun testGetGenericObject() {
    InjectTestResources().apply {
      val testObject2: TestGeneric<TestDependencyInterface> = mockspresso.buildUpon()
          .injectByJavaxConfig()
          .testResources(this)
          .build()
          .getDependencyOf()!!

      assertThat(testObject2)
          .isNotNull
          .isNot(mockCondition())
          .isEqualTo(testGeneric)
    }
  }

  @Test fun testGenericInjection() {
    val testObject2 = TestGeneric<TestDependencyInterface>()
    mockspresso.buildUpon()
        .injectByJavaxConfig()
        .build()
        .injectType(testObject2)

    assertThat(testObject2.dep)
        .isEqualTo(testDependency)
  }
}
