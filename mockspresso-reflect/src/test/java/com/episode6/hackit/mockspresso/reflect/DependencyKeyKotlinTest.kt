package com.episode6.hackit.mockspresso.reflect

import com.episode6.hackit.mockspresso.reflect.testobject.JavaTokens
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 *
 */
class DependencyKeyKotlinTest {

  @Test fun testStringKey() {
    val key = dependencyKey<String>()

    assertThat(key).isEqualTo(JavaTokens.stringKey)
  }

  @Test fun testNamedIntKey() {
    val namedKey = dependencyKey<Int>(NamedAnnotationLiteral("intKey"))
    val unnamedKey = dependencyKey<Int>()

    assertThat(JavaTokens.intKey)
        .isEqualTo(namedKey)
        .isNotEqualTo(unnamedKey)
  }

  @Test fun testStringListKey() {
    val key = dependencyKey<List<String>>()

    assertThat(key).isEqualTo(JavaTokens.stringListKey)
  }

  @Test fun testStringIntMapKey() {
    val key = dependencyKey<Map<String, Int>>()

    assertThat(key).isEqualTo(JavaTokens.stringIntMapKey)
  }

  @Test fun testStringMutableListKey() {
    val key =  dependencyKey<MutableList<String>>()

    assertThat(key).isEqualTo(JavaTokens.stringMutableListKey)
  }

  @Test fun testStringIntMutableMapKey() {
    val key =  dependencyKey<MutableMap<String, Int>>()

    assertThat(key).isEqualTo(JavaTokens.stringIntMutableMapKey)
  }

  @Test fun testStringLinkedListKey() {
    val key =  dependencyKey<LinkedList<String>>()

    assertThat(key).isEqualTo(JavaTokens.stringLinkedListKey)
  }

  @Test fun testStringIntHashMapKey() {
    val key =  dependencyKey<HashMap<String, Int>>()

    assertThat(key).isEqualTo(JavaTokens.stringIntHashMapKey)
  }
}
