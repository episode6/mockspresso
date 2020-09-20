package com.episode6.hackit.mockspresso.reflect

import com.episode6.hackit.mockspresso.reflect.testobject.JavaTokens
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*
import javax.inject.Provider

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
    val key = dependencyKey<MutableList<String>>()

    assertThat(key).isEqualTo(JavaTokens.stringMutableListKey)
  }

  @Test fun testStringIntMutableMapKey() {
    val key = dependencyKey<MutableMap<String, Int>>()

    assertThat(key).isEqualTo(JavaTokens.stringIntMutableMapKey)
  }

  @Test fun testStringLinkedListKey() {
    val key = dependencyKey<LinkedList<String>>()

    assertThat(key).isEqualTo(JavaTokens.stringLinkedListKey)
  }

  @Test fun testStringIntHashMapKey() {
    val key = dependencyKey<HashMap<String, Int>>()

    assertThat(key).isEqualTo(JavaTokens.stringIntHashMapKey)
  }

  @Suppress("UNCHECKED_CAST")
  @Test fun testGenericParameterProviderKey() {
    val unNamedKey = dependencyKey<Provider<String>>()
    val namedKey = dependencyKey<Provider<String>>(NamedAnnotationLiteral("hello"))

    val unNamedParamKey = unNamedKey.genericParameterKey()!!
    val namedParamKey = namedKey.genericParameterKey()!!

    assertThat(unNamedParamKey)
        .isEqualTo(JavaTokens.stringKey as DependencyKey<Any>)
        .isNotEqualTo(namedParamKey)
    assertThat(namedParamKey.typeToken).isEqualTo(JavaTokens.stringToken as TypeToken<Any>)
    assertThat(namedParamKey.identityAnnotation).isEqualTo(namedKey.identityAnnotation)
    assertThat(unNamedParamKey.identityAnnotation).isNull()
  }

  @Suppress("UNCHECKED_CAST")
  @Test fun testGenericParameterProviderKeyWithChildGeneric() {
    val unNamedKey = dependencyKey<Provider<List<String>>>()
    val namedKey = dependencyKey<Provider<List<String>>>(NamedAnnotationLiteral("hello"))

    val unNamedParamKey = unNamedKey.genericParameterKey()!!
    val namedParamKey = namedKey.genericParameterKey()!!

    assertThat(unNamedParamKey)
        .isEqualTo(JavaTokens.stringListKey as DependencyKey<Any>)
        .isNotEqualTo(namedParamKey)
    assertThat(namedParamKey.typeToken).isEqualTo(JavaTokens.stringListToken as TypeToken<Any>)
    assertThat(namedParamKey.identityAnnotation).isEqualTo(namedKey.identityAnnotation)
    assertThat(unNamedParamKey.identityAnnotation).isNull()
  }
}
