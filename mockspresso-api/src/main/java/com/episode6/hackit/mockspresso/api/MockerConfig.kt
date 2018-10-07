package com.episode6.hackit.mockspresso.api

import com.episode6.hackit.mockspresso.reflect.TypeToken

/**
 * A config interface used by mockspresso to handle to mocking of objects using
 * any mocker that supports annotations.
 */
interface MockerConfig {

  /**
   * Class that creates mocks, used for automatically generating mocks that
   * are not part of the dependency map
   */
  interface MockMaker {
    fun <T> makeMock(typeToken: TypeToken<T>): T
  }

  /**
   * Class that prepares the mocks on an object (usually an instance of
   * the current test class, but can be any generic pojo with annotated fields)
   */
  interface FieldPreparer {
    fun prepareFields(objectWithMockFields: Any)
  }

  /**
   * @return The MockMaker to be used by this config
   */
  fun provideMockMaker(): MockMaker

  /**
   * @return The FieldPreparer to be used by this config.
   */
  fun provideFieldPreparer(): FieldPreparer

  /**
   * @return a list of annotations that identify objects as mocks (or spys or whatever).
   * Fields with these annotations should be set my the FieldPreparer, and mockspresso will
   * pull their values and add them to the dependency map.
   */
  fun provideMockAnnotations(): List<Class<out Annotation>>
}
