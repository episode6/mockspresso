package com.episode6.hackit.mockspresso.mockito.powermock

import com.episode6.hackit.mockspresso.api.MockerConfig
import com.episode6.hackit.mockspresso.api.MockerConfig.FieldPreparer
import com.episode6.hackit.mockspresso.reflect.ReflectUtil
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.powermock.api.mockito.PowerMockito
import java.lang.reflect.Field

/**
 * A MockerConfig for Powermock + Mockito
 */
internal class PowerMockitoConfig : MockerConfig, FieldPreparer, MockerConfig.MockMaker {
  override fun provideMockMaker(): MockerConfig.MockMaker = this
  override fun provideFieldPreparer(): FieldPreparer = this
  override fun provideMockAnnotations(): List<Class<out Annotation?>> = listOf(Mock::class.java, Spy::class.java)

  @Suppress("UNCHECKED_CAST")
  override fun <T> makeMock(typeToken: TypeToken<T>): T = PowerMockito.mock(typeToken.rawType as Class<T>)

  override fun prepareFields(objectWithMockFields: Any) {
    ReflectUtil.getAllDeclaredFields(objectWithMockFields.javaClass).forEach { field ->
      if (!field.isAccessible) field.isAccessible = true
      val value = field[objectWithMockFields]
      when {
        value == null && field.isMock() -> field[objectWithMockFields] = PowerMockito.mock(field.type)
        value == null && field.isSpy()  -> field[objectWithMockFields] = Mockito.spy(field.type)
        value != null && field.isSpy()  -> field[objectWithMockFields] = Mockito.spy(value)
      }
    }
  }
}

private fun Field.isMock() = isAnnotationPresent(Mock::class.java)
private fun Field.isSpy() = isAnnotationPresent(Spy::class.java)
