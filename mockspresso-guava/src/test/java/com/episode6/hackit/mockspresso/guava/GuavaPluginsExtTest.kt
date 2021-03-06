@file:Suppress("DEPRECATION")

package com.episode6.hackit.mockspresso.guava

import com.episode6.hackit.mockspresso.Mockspresso
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.stubbing.Answer

/**
 * Tests [com.episode6.hackit.mockspresso.dagger.DaggerPluginsExtKt]
 */
class GuavaPluginsExtTest {

  val builder: Mockspresso.Builder = mock(defaultAnswer = Answer { it.mock })

  @Test
  fun testListenableFutureSourceOfTruth() {
    val result = builder.automaticListenableFutures()

    assertThat(result).isEqualTo(builder)
    verify(builder).specialObjectMaker(any<ListenableFutureMaker>())
  }

  @Test
  fun testSupplierSourceOfTruth() {
    val result = builder.automaticSuppliers()

    assertThat(result).isEqualTo(builder)
    verify(builder).specialObjectMaker(any<SupplierMaker>())
  }
}
