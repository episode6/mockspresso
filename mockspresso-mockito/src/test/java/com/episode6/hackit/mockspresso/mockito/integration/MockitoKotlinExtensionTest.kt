package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.annotation.Dependency
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin
import com.episode6.hackit.mockspresso.dependencyOf
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import javax.inject.Named

/**
 * Testing kotlin extensions with mockito
 */
class MockitoKotlinExtensionTest {

  private interface TestInterface
  private class TestDependency : TestInterface
  private class TestObjectWithConcrete(val testDep: TestDependency)
  private class TestObjectWithInterface(val testDep: TestInterface)
  private class TestObjectWithAnnotatedInterface(@Named("testing") val testDep: TestInterface)
  private class TestResources {
    @Dependency(bindAs = TestInterface::class) @field:Named("testing") val testDependency = TestDependency()
  }

  @get:Rule val mockspresso = BuildMockspresso.with()
      .plugin(SimpleInjectMockspressoPlugin())
      .plugin(MockitoPlugin())
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
        .dependencyOf<TestInterface> { testDependency }
        .build()
        .create(TestObjectWithInterface::class.java)

    assertThat(testObject.testDep).isEqualTo(testDependency)
  }

  @Test fun testNamedInterfaceDependency() {
    // we can't define an annotation literal in kotlin, but we can use one
    // that is defined in java
    val testObject: TestObjectWithAnnotatedInterface = mockspresso.buildUpon()
        .dependencyOf<TestInterface>(qualifier = NamedAnnotationLiteral("testing")) { testDependency }
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
}
