package com.episode6.hackit.mockspresso.reflect

import com.episode6.hackit.mockspresso.reflect.testobject.JavaTypeTokens
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * Tests the TypeToken class
 */
class TypeTokenKotlinTest {

  @Test fun testStringToken() {
    val token = typeToken<String>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringToken)
  }

  @Test fun testIntToken() {
    val token = typeToken<Int>()

    assertThat(token).isEqualTo(JavaTypeTokens.intToken)
  }

  @Test fun testStringListToken() {
    val token = typeToken<List<String>>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringListToken)
  }

  @Test fun testStringIntMapTokenToken() {
    val token = typeToken<Map<String, Int>>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringIntMapToken)
  }

  @Test fun testStringLinkedListToken() {
    val token = typeToken<LinkedList<String>>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringLinkedListToken)
  }

  @Test fun testStringIntHashMapTokenToken() {
    val token = typeToken<HashMap<String, Int>>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringIntHashMapToken)
  }
}
