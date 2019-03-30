package com.episode6.hackit.mockspresso.reflect

import com.episode6.hackit.mockspresso.reflect.testobject.JavaTokens
import com.episode6.hackit.mockspresso.reflect.testobject.TestGenericKtInterface
import com.episode6.hackit.mockspresso.reflect.testobject.TestJavaObjectWithKtGeneric
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * Tests the TypeToken class
 */
class TypeTokenKotlinTest {

  @Test fun testStringToken() {
    val token = typeToken<String>()

    assertThat(token).isEqualTo(JavaTokens.stringToken)
  }

  @Test fun testIntToken() {
    val token = typeToken<Int>()

    assertThat(token).isEqualTo(JavaTokens.intToken)
  }

  @Test fun testStringListToken() {
    val token = typeToken<List<String>>()

    assertThat(token).isEqualTo(JavaTokens.stringListToken)
  }

  @Test fun testStringIntMapToken() {
    val token = typeToken<Map<String, Int>>()

    assertThat(token).isEqualTo(JavaTokens.stringIntMapToken)
  }

  @Test fun testStringMutableListToken() {
    val token = typeToken<MutableList<String>>()

    assertThat(token).isEqualTo(JavaTokens.stringMutableListToken)
  }

  @Test fun testStringIntMutableMapToken() {
    val token = typeToken<MutableMap<String, Int>>()

    assertThat(token).isEqualTo(JavaTokens.stringIntMutableMapToken)
  }

  @Test fun testStringLinkedListToken() {
    val token = typeToken<LinkedList<String>>()

    assertThat(token).isEqualTo(JavaTokens.stringLinkedListToken)
  }

  @Test fun testStringIntHashMapToken() {
    val token = typeToken<HashMap<String, Int>>()

    assertThat(token).isEqualTo(JavaTokens.stringIntHashMapToken)
  }

  /**
   * This test basically documents a kind of broken pattern in kotlin/java interop
   * where mockspresso is concerned. We're parsing the TypeToken from a java class, but
   * the field type is a generic Kotlin interface, and the declaration of the field ignores
   * the `out` keyword on the type variable (i.e. it doesn't include `? extends` like
   * generated kotlin code would).
   *
   * As a result these two tokens that *should* match, don't. We test just so we can have signal
   * if/when this behavior changes, and to document it.
   */
  @Test fun testKotlinGenericWithOutTypeFails() {
    // the `out` keyword on `TestGenericKtInterface's TypeVariable, means this token  actually
    // looks like `TestGenericKtInterface<? extends String>`
    val manualToken = typeToken<TestGenericKtInterface<String>>()

    // this class (written in java) delclares a field that *should* match the token above,
    // but doesn't because its missing `? extends`
    val testObj = TestJavaObjectWithKtGeneric()
    val fieldToken = TypeToken.of(testObj.genericIfaceField.genericType)

    // we test this just so we can know if/when this behavior ever changes
    // ideally we actually want these two tokens to be equal.
    assertThat(fieldToken).isNotEqualTo(manualToken)
  }

  /**
   * This test just demonstrates that we can use typeTokens defined in java to work
   * around the issue described in the above test.
   */
  @Test fun testKotlinGenericWithoutTypeMatchesJavaToken() {
    // if we create the the TypeToken in a java file in the same incorrect way the
    // TestJavaObjectWithKtGeneric defines it's field, the tokens now match.
    val manualJavaToken = JavaTokens.genericInterfaceIllegalToken
    val testObj = TestJavaObjectWithKtGeneric()
    val fieldToken = TypeToken.of(testObj.genericIfaceField.genericType)

    assertThat(fieldToken).isEqualTo(manualJavaToken)
  }
}
