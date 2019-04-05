package com.episode6.hackit.mockspresso.basic.plugin

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.stubbing.Answer


/**
 * Tests [com.episode6.hackit.mockspresso.basic.plugin.BasicPluginsExtKt]
 */
class BasicPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test fun testSimplePluginSourceOfTruth() {
    builder.injectWithSimpleConfig()

    verify(builder).plugin(any<SimpleInjectMockspressoPlugin>())
  }

  @Test fun testJavaxPluginSourceOfTruth() {
    builder.injectWithJavaxConfig()

    verify(builder).plugin(any<JavaxInjectMockspressoPlugin>())
  }
}
