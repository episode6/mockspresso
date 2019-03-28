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
    // using a MutableList causes the type to be cast to List without the `out` type variable
    // thus this test starts passing
    val token = typeToken<MutableList<String>>()

    assertThat(token).isEqualTo(JavaTypeTokens.stringListToken)
  }

  @Test fun testStringListTokenManual() {
    val token: TypeToken<MutableList<String>> = object : TypeToken<MutableList<String>>() {}

    assertThat(token).isEqualTo(JavaTypeTokens.stringListToken)
  }

  @Test fun testStringIntMapTokenToken() {
    val token = typeToken<MutableMap<String, Int>>()

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
