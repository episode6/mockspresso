package com.episode6.hackit.mockspresso.api

import com.episode6.hackit.mockspresso.reflect.TypeToken
import java.lang.reflect.Constructor

/**
 * A configuration interface that defines how we construct real objects.
 */
interface InjectionConfig {
  fun provideInjectableFieldAnnotations(): List<Class<out Annotation>>
  fun provideInjectableMethodAnnotations(): List<Class<out Annotation>>
  fun <T> chooseConstructor(typeToken: TypeToken<T>): Constructor<T>?
}
