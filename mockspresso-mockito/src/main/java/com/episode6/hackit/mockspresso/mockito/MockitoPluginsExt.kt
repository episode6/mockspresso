package com.episode6.hackit.mockspresso.mockito

import com.episode6.hackit.mockspresso.Mockspresso
import kotlin.reflect.KClass

/**
 * Kotlin extension methods for mockspresso's mockito plugins
 */

/**
 * Applies the [com.episode6.hackit.mockspresso.api.MockerConfig] to support mockito
 */
fun Mockspresso.Builder.mocksByMockito(): Mockspresso.Builder = plugin(MockitoPlugin())

/**
 * Applies special object handling for the provided factory classes. The
 * applicable objects will be mocked by mockito, but with a default answer
 * that returns objects from mockspresso's dependency map (similar to how
 * the javax() injector automatically binds Providers, but applied to any
 * factory class, including generics).
 */
fun Mockspresso.Builder.automaticFactories(vararg classes: Class<*>): Mockspresso.Builder =
    specialObjectMaker(MockitoAutoFactoryMaker.create(*classes))

/**
 * Convenience function to call [automaticFactories] using kotlin KClasses instead of java Classes
 */
fun Mockspresso.Builder.automaticFactories(vararg classes: KClass<*>): Mockspresso.Builder =
    automaticFactories(*classes.map { it.java }.toTypedArray())
