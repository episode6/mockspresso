package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.createNew
import com.episode6.hackit.mockspresso.exception.BrokenSpecialObjectMakerException
import com.episode6.hackit.mockspresso.mockito.mockByMockito
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.api.Fail.fail
import org.junit.Test

class SpecialObjectCastFailureTest {

  @Test fun testBadSpecialObjectMaker() {
    val mockspresso = BuildMockspresso.with()
        .injectBySimpleConfig()
        .mockByMockito()
        .specialObjectMaker(BadSpecialObjectMaker())
        .build()

    try {
      mockspresso.createNew<TestClass>()
      fail("exception expected")
    } catch (e: BrokenSpecialObjectMakerException) {
      assertThat(e.message).isEqualTo("Special object maker returned an invalid object. SpecialObjectMaker: com.episode6.hackit.mockspresso.mockito.integration.BadSpecialObjectMaker, expected: DependencyKey{typeToken=int}, but object returned type: java.lang.String, value: fail")
    }

  }
}

class BadSpecialObjectMaker : SpecialObjectMaker {
  override fun canMakeObject(key: DependencyKey<*>): Boolean = true
  override fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any? = "fail"
}

class TestClass(val intDep: Int)
