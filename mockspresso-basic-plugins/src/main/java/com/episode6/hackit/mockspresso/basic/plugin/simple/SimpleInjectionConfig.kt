package com.episode6.hackit.mockspresso.basic.plugin.simple

import com.episode6.hackit.mockspresso.api.InjectionConfig
import com.episode6.hackit.mockspresso.reflect.TypeToken
import java.lang.reflect.Constructor

/**
 * A very simple implementation of InjectionConfig. Chooses the constructor with the least number of arguments,
 * and provides no injectable field annotations.
 */
internal class SimpleInjectionConfig : InjectionConfig, InjectionConfig.ConstructorSelector {
  override fun provideConstructorSelector(): InjectionConfig.ConstructorSelector = this
  override fun provideInjectableFieldAnnotations(): List<Class<out Annotation?>> = emptyList()
  override fun provideInjectableMethodAnnotations(): List<Class<out Annotation?>> = emptyList()

  @Suppress("UNCHECKED_CAST")
  override fun <T> chooseConstructor(typeToken: TypeToken<T>): Constructor<T>? = typeToken.rawType.declaredConstructors
      .sortedBy { it.parameterCount }
      .firstOrNull() as Constructor<T>?
}
