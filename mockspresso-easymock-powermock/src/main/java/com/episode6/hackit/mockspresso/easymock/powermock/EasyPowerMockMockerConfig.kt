package com.episode6.hackit.mockspresso.easymock.powermock

import com.episode6.hackit.mockspresso.api.MockerConfig
import com.episode6.hackit.mockspresso.api.MockerConfig.FieldPreparer
import com.episode6.hackit.mockspresso.reflect.ReflectUtil
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.easymock.EasyMock
import org.powermock.api.easymock.PowerMock
import java.lang.reflect.Field

/**
 * A MockerConfig for PowerMock + EasyMock
 */
internal class EasyPowerMockMockerConfig : MockerConfig, FieldPreparer, MockerConfig.MockMaker {
  override fun provideMockMaker(): MockerConfig.MockMaker = this
  override fun provideFieldPreparer(): FieldPreparer = this

  override fun provideMockAnnotations(): List<Class<out Annotation?>> = listOf(
      org.easymock.Mock::class.java,
      org.powermock.api.easymock.annotation.Mock::class.java,
      org.powermock.api.easymock.annotation.MockStrict::class.java,
      org.powermock.api.easymock.annotation.MockNice::class.java)


  @Suppress("UNCHECKED_CAST") // Using a strict mock by default here doesn't make sense for an auto-mocker
  override fun <T> makeMock(typeToken: TypeToken<T>): T = PowerMock.createNiceMock(typeToken.rawType as Class<T>).also {
    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(it)
  }

  override fun prepareFields(objectWithMockFields: Any) {
    ReflectUtil.getAllDeclaredFields(objectWithMockFields.javaClass).forEach { field ->
      if (!field.isAccessible) field.isAccessible = true
      if (field[objectWithMockFields] != null) return@forEach
      when {
        field.isMock()       -> field[objectWithMockFields] = PowerMock.createMock(field.type)
        field.isNiceMock()   -> field[objectWithMockFields] = PowerMock.createNiceMock(field.type)
        field.isStrictMock() -> field[objectWithMockFields] = PowerMock.createStrictMock(field.type)
      }
    }
  }
}

private fun Field.isMock() = isAnnotationPresent(org.easymock.Mock::class.java) || isAnnotationPresent(org.powermock.api.easymock.annotation.Mock::class.java)
private fun Field.isNiceMock() = isAnnotationPresent(org.powermock.api.easymock.annotation.MockNice::class.java)
private fun Field.isStrictMock() = isAnnotationPresent(org.powermock.api.easymock.annotation.MockStrict::class.java)
