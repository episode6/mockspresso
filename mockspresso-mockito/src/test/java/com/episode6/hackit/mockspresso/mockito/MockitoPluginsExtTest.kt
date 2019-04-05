package com.episode6.hackit.mockspresso.mockito

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.reflect.dependencyKey
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.stubbing.Answer
import javax.inject.Provider

/**
 * Tests [com.episode6.hackit.mockspresso.mockito.MockitoPluginsExtKt]
 */
class MockitoPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test fun testMockPluginSourceOfTruth() {
    builder.mockWithMockito()

    verify(builder).plugin(any<MockitoPlugin>())
  }

  @Test fun testAutomaticFactorySourceOfTruthKotlin() {
    builder.automaticFactories(Provider::class, HashMap::class)

    argumentCaptor<MockitoAutoFactoryMaker> {
      verify(builder).specialObjectMaker(capture())

      assertThat(firstValue.canMakeObject(dependencyKey<Provider<String>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<HashMap<String, Int>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<Long>())).isFalse
    }
  }

  @Test fun testAutomaticFactorySourceOfTruthJava() {
    builder.automaticFactories(Provider::class.java, HashMap::class.java)

    argumentCaptor<MockitoAutoFactoryMaker> {
      verify(builder).specialObjectMaker(capture())

      assertThat(firstValue.canMakeObject(dependencyKey<Provider<String>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<HashMap<String, Int>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<Long>())).isFalse
    }
  }
}
