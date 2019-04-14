package com.episode6.hackit.mockspresso.dagger

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.InjectionConfig
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.nhaarman.mockitokotlin2.*
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.api.Assertions.atIndex
import org.fest.assertions.core.Condition
import org.junit.Test
import org.mockito.stubbing.Answer

/**
 * Tests [com.episode6.hackit.mockspresso.dagger.DaggerPluginsExtKt]
 */
class DaggerPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test
  fun testInjectByDaggerSourceOfTruth() {
    val result = builder.injectByDaggerConfig()

    assertThat(result).isEqualTo(builder)
    argumentCaptor<InjectionConfig>() {
      verify(builder).injector(capture())
      assertThat(firstValue).has(className("JavaxInjectionConfig"))
    }
    argumentCaptor<SpecialObjectMaker>() {
      verify(builder, times(2)).specialObjectMaker(capture())
      assertThat(allValues)
          .has(className("ProviderMaker"), atIndex(0))
          .has(className("DaggerLazyMaker"), atIndex(1))
    }
  }

  @Test fun testAutomaticLaziesSourceOfTruth() {
    val result = builder.automaticLazies()

    assertThat(result).isEqualTo(builder)
    verify(builder).specialObjectMaker(any<DaggerLazyMaker>())
  }
}

private fun <T: Any> className(simpleName: String) = object : Condition<T>("expected class named $simpleName") {
  override fun matches(value: T?): Boolean = value!!.javaClass.simpleName == simpleName
}
