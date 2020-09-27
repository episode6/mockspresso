package com.episode6.hackit.mockspresso.basic.plugin.javax

import com.episode6.hackit.mockspresso.api.InjectionConfig
import com.episode6.hackit.mockspresso.reflect.TypeToken
import java.lang.reflect.Constructor
import javax.inject.Inject

/**
 * An implementation of [InjectionConfig] designed for the javax.inject package.
 * With this config, we look for the constructor annotated with @Inject, and prefer it.
 * If no @Inject constructor is found, we will use and empty constructor, if one is found.
 * If multiple @Inject constructors are found, an exception will be thrown.
 */
internal class JavaxInjectionConfig : InjectionConfig {
  override fun provideInjectableFieldAnnotations(): List<Class<out Annotation>> = listOf(Inject::class.java)
  override fun provideInjectableMethodAnnotations(): List<Class<out Annotation>> = listOf(Inject::class.java)

  @Suppress("UNCHECKED_CAST")
  override fun <T> chooseConstructor(typeToken: TypeToken<T>): Constructor<T>? {
    val injectConstructors = typeToken.rawType.declaredConstructors.filter { it.isAnnotationPresent(Inject::class.java) }
    return when (injectConstructors.size) {
      0    -> typeToken.rawType.declaredConstructors.firstOrNull { it.parameterCount == 0 }
      1    -> injectConstructors[0]
      else -> throw MultipleInjectConstructorException(typeToken)
    } as Constructor<T>?
  }
}
