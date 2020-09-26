package com.episode6.hackit.mockspresso.mockito

import com.episode6.hackit.mockspresso.api.MockerConfig
import com.episode6.hackit.mockspresso.api.MockerConfig.FieldPreparer
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

/**
 * A MockerConfig for Mockito.
 *
 */
internal class MockitoMockerConfig : MockerConfig, MockerConfig.MockMaker, FieldPreparer {
  override fun provideMockMaker(): MockerConfig.MockMaker = this
  override fun provideFieldPreparer(): FieldPreparer = this
  override fun provideMockAnnotations(): List<Class<out Annotation?>> = listOf(Mock::class.java, Spy::class.java)


  override fun prepareFields(objectWithMockFields: Any) {
    @Suppress("DEPRECATION") // TODO: Define new mocker config that leverages openMocks properly
    MockitoAnnotations.initMocks(objectWithMockFields)
  }

  @Suppress("UNCHECKED_CAST") override fun <T> makeMock(typeToken: TypeToken<T>): T =
      Mockito.mock(typeToken.rawType as Class<T>)
}
