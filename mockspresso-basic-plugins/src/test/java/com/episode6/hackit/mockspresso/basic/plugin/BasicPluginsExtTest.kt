package com.episode6.hackit.mockspresso.basic.plugin

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectionConfig
import com.episode6.hackit.mockspresso.basic.plugin.javax.ProviderMaker
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectionConfig
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.stubbing.Answer


/**
 * Tests [com.episode6.hackit.mockspresso.basic.plugin.BasicPluginsExtKt]
 */
class BasicPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test fun testSimplePluginSourceOfTruth() {
    val result = builder.injectBySimpleConfig()

    assertThat(result).isEqualTo(builder)
    verify(builder).injector(any<SimpleInjectionConfig>())
  }

  @Test fun testJavaxPluginSourceOfTruth() {
    val result = builder.injectByJavaxConfig()

    assertThat(result).isEqualTo(builder)
    verify(builder).injector(any<JavaxInjectionConfig>())
    verify(builder).specialObjectMaker(any<ProviderMaker>())
  }

  @Test fun testAutomaticProvidersSourceOfTruth() {
    val result = builder.automaticProviders()

    assertThat(result).isEqualTo(builder)
    verify(builder).specialObjectMaker(any<ProviderMaker>())
  }
}
