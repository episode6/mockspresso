@file:Suppress("DEPRECATION")

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
    val result = builder.mockByMockito()

    assertThat(result).isEqualTo(builder)
    verify(builder).mocker(any<MockitoMockerConfig>())
  }

  @Test fun testAutomaticFactorySourceOfTruthKotlin() {
    val result = builder.automaticFactories(Provider::class, HashMap::class)

    assertThat(result).isEqualTo(builder)
    argumentCaptor<MockitoAutoFactoryMaker> {
      verify(builder).specialObjectMaker(capture())

      assertThat(firstValue.canMakeObject(dependencyKey<Provider<String>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<HashMap<String, Int>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<Long>())).isFalse
    }
  }

  @Test fun testAutomaticFactorySourceOfTruthJava() {
    val result = builder.automaticFactories(Provider::class.java, HashMap::class.java)

    assertThat(result).isEqualTo(builder)
    argumentCaptor<MockitoAutoFactoryMaker> {
      verify(builder).specialObjectMaker(capture())

      assertThat(firstValue.canMakeObject(dependencyKey<Provider<String>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<HashMap<String, Int>>())).isTrue
      assertThat(firstValue.canMakeObject(dependencyKey<Long>())).isFalse
    }
  }
}
