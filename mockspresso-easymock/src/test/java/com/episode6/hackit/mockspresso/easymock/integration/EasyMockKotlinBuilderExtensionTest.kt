package com.episode6.hackit.mockspresso.easymock.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.annotation.Dependency
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.dependencyOf
import com.episode6.hackit.mockspresso.easymock.Conditions.mockCondition
import com.episode6.hackit.mockspresso.easymock.mockByEasyMock
import com.episode6.hackit.mockspresso.realClassOf
import com.episode6.hackit.mockspresso.realImplOf
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral
import com.episode6.hackit.mockspresso.testing.isConcreteInstanceOf
import com.episode6.hackit.mockspresso.testing.satisfies
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Named

/**
 * Testing kotlin extensions to [com.episode6.hackit.mockspresso.Mockspresso.Builder] with easymock
 */
class EasyMockKotlinBuilderExtensionTest {

  private interface TestDependencyInterface
  private interface TestObjectInterface
  private class TestDependency : TestDependencyInterface

  private class TestObjectWithConcrete(val testDep: TestDependency) : TestObjectInterface
  private class TestObjectWithInterface(val testDep: TestDependencyInterface)
  private class TestObjectWithAnnotatedInterface(@Named("testing") val testDep: TestDependencyInterface)
  private class TestResources {
    @Dependency(bindAs = TestDependencyInterface::class) @field:Named("testing") val testDependency = TestDependency()
  }

  private class OuterTestObjectWithConcreteObj(val testObj: TestObjectWithConcrete)
  private class OuterTestObjectWithInterface(val testObj: TestObjectInterface)
  private class OuterTestObjectWithAnnotatedInterface(@Named("testing")  val testObj: TestObjectInterface)

  @get:Rule val mockspresso = BuildMockspresso.with()
      .injectBySimpleConfig()
      .mockByEasyMock()
      .buildRule()

  private val testDependency = TestDependency()

  @Test fun testSimpleDependency() {
    val testObject: TestObjectWithConcrete = mockspresso.buildUpon()
        .dependencyOf { testDependency }
        .build()
        .create(TestObjectWithConcrete::class.java)

    assertThat(testObject.testDep).isEqualTo(testDependency)
  }

  @Test fun testInterfaceDependency() {
    val testObject: TestObjectWithInterface = mockspresso.buildUpon()
        .dependencyOf<TestDependencyInterface> { testDependency }
        .build()
        .create(TestObjectWithInterface::class.java)

    assertThat(testObject.testDep).isEqualTo(testDependency)
  }

  @Test fun testNamedInterfaceDependency() {
    // we can't define an annotation literal in kotlin, but we can use one
    // that is defined in java
    val testObject: TestObjectWithAnnotatedInterface = mockspresso.buildUpon()
        .dependencyOf<TestDependencyInterface>(qualifier = NamedAnnotationLiteral("testing")) { testDependency }
        .build()
        .create(TestObjectWithAnnotatedInterface::class.java)

    assertThat(testObject.testDep).isEqualTo(testDependency)
  }

  @Test fun testNamedInterfaceDependencyWithTestResources() {
    // we can still use field annotations on tests/test resources if we
    // want to avoid annotation literals
    val testResources = TestResources()
    val testObject: TestObjectWithAnnotatedInterface = mockspresso.buildUpon()
        .testResources(testResources)
        .build()
        .create(TestObjectWithAnnotatedInterface::class.java)

    assertThat(testObject.testDep).isEqualTo(testResources.testDependency)
  }

  @Test fun testRealClassOf() {
    val outerObj: OuterTestObjectWithConcreteObj = mockspresso.buildUpon()
        .realClassOf<TestObjectWithConcrete>()
        .dependencyOf { testDependency }
        .build()
        .create(OuterTestObjectWithConcreteObj::class.java)

    assertThat(outerObj.testObj)
        .isNot(mockCondition())
        .isConcreteInstanceOf<TestObjectWithConcrete>()
        .satisfies {
          assertThat(it.testDep).isEqualTo(testDependency)
        }
  }

  @Test fun testRealImplOf() {
    val outerObj: OuterTestObjectWithInterface = mockspresso.buildUpon()
        .realImplOf<TestObjectInterface, TestObjectWithConcrete>()
        .dependencyOf { testDependency }
        .build()
        .create(OuterTestObjectWithInterface::class.java)

    assertThat(outerObj.testObj)
        .isNot(mockCondition())
        .isConcreteInstanceOf<TestObjectWithConcrete>()
        .satisfies {
          assertThat(it.testDep).isEqualTo(testDependency)
        }
  }

  @Test fun testAnnotatedRealImplOf() {
    val outerObj: OuterTestObjectWithAnnotatedInterface = mockspresso.buildUpon()
        .realImplOf<TestObjectInterface, TestObjectWithConcrete>(qualifier = NamedAnnotationLiteral("testing"))
        .dependencyOf { testDependency }
        .build()
        .create(OuterTestObjectWithAnnotatedInterface::class.java)

    assertThat(outerObj.testObj)
        .isNot(mockCondition())
        .isConcreteInstanceOf<TestObjectWithConcrete>()
        .satisfies {
          assertThat(it.testDep).isEqualTo(testDependency)
        }
  }
}
