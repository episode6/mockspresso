package com.episode6.hackit.mockspresso.mockito

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.api.MockspressoPlugin
import kotlin.reflect.KClass

/**
 * Kotlin extension methods for mockspresso's mockito plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] to support mockito
 */
@JvmSynthetic
fun Mockspresso.Builder.mockByMockito(): Mockspresso.Builder = mocker(MockitoMockerConfig())

/**
 * Applies special object handling for the provided factory classes. The
 * applicable objects will be mocked by mockito, but with a default answer
 * that returns objects from mockspresso's dependency map (similar to how
 * the javax() injector automatically binds Providers, but applied to any
 * factory class, including generics).
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticFactories(vararg classes: Class<*>): Mockspresso.Builder =
    specialObjectMaker(MockitoAutoFactoryMaker.create(*classes))

/**
 * Convenience function to call [automaticFactories] using kotlin KClasses instead of java Classes
 */
@JvmSynthetic
fun Mockspresso.Builder.automaticFactories(vararg classes: KClass<*>): Mockspresso.Builder =
    automaticFactories(*classes.map { it.java }.toTypedArray())

/**
 * Expose the extension methods defined here as [MockspressoPlugin]s for consumption by java tests
 */
@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")
@SinceKotlin("9999.0")
object MockspressoMockitoPluginsJavaSupport {
  @JvmStatic fun mockByMockito(): MockspressoPlugin = MockspressoPlugin { it.mockByMockito() }
  @JvmStatic fun automaticFactories(vararg classes: Class<*>): MockspressoPlugin = MockspressoPlugin { it.automaticFactories(*classes) }
}
