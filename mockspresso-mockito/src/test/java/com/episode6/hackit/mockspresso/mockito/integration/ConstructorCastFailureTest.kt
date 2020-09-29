package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.api.InjectionConfig
import com.episode6.hackit.mockspresso.createNew
import com.episode6.hackit.mockspresso.exception.BrokenInjectionConfigException
import com.episode6.hackit.mockspresso.mockito.mockByMockito
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.api.Assertions.fail
import org.junit.Test
import java.lang.reflect.Constructor

class ConstructorCastFailureTest {
  @Test fun testBadInjectionConfig() {
    val mockspresso = BuildMockspresso.with()
        .injector(BadInjectConfig())
        .mockByMockito()
        .build()

    try {
      mockspresso.createNew<ConstructorTestClass>()
      fail("expected an exception")
    } catch (e: BrokenInjectionConfigException) {
      assertThat(e.message).startsWith("InjectionConfig returned an invalid constructor. InjectionConfig: com.episode6.hackit.mockspresso.mockito.integration.BadInjectConfig, expected: com.episode6.hackit.mockspresso.mockito.integration.ConstructorTestClass, but constructor resulted in object of type: com.episode6.hackit.mockspresso.mockito.integration.OtherConstructorTestClass, value: com.episode6.hackit.mockspresso.mockito.integration.OtherConstructorTestClass@")
    }
  }
}

class ConstructorTestClass()
class OtherConstructorTestClass()

class BadInjectConfig : InjectionConfig {
  override fun provideInjectableFieldAnnotations(): List<Class<out Annotation>> = emptyList()
  override fun provideInjectableMethodAnnotations(): List<Class<out Annotation>> = emptyList()
  override fun chooseConstructor(typeToken: TypeToken<*>): Constructor<out Any>? = OtherConstructorTestClass::class.java.declaredConstructors.first()

}
