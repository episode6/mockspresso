package com.episode6.hackit.mockspresso.easymock

import com.episode6.hackit.mockspresso.api.MockerConfig
import com.episode6.hackit.mockspresso.api.MockerConfig.FieldPreparer
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.easymock.EasyMock
import org.easymock.EasyMockSupport
import org.easymock.Mock

/**
 * A MockerConfig for EasyMock
 */
internal class EasyMockMockerConfig : MockerConfig, MockerConfig.MockMaker, FieldPreparer {
  override fun provideMockMaker(): MockerConfig.MockMaker = this
  override fun provideFieldPreparer(): FieldPreparer = this
  override fun provideMockAnnotations(): List<Class<out Annotation?>> = listOf(Mock::class.java)

  @Suppress("UNCHECKED_CAST") // Using a strict mock by default here doesn't make sense for an auto-mocker
  override fun <T> makeMock(typeToken: TypeToken<T>): T = EasyMock.niceMock<T>(typeToken.rawType as Class<T>).also {
    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(it)
  }

  override fun prepareFields(objectWithMockFields: Any) {
    EasyMockSupport.injectMocks(objectWithMockFields)
  }
}
