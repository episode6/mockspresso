package com.episode6.hackit.mockspresso.exception

import com.episode6.hackit.mockspresso.api.InjectionConfig
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.TypeToken

/**
 * Error thrown when trying to declare multiple @RealObjects that create a circular dependency.
 */
class CircularDependencyError(key: DependencyKey<*>) : VerifyError("Attempted to create circular dependency of @RealObjects failed on: $key")

/**
 * Exception thrown when trying to create a real object that has no constructor.
 */
class NoValidConstructorException(typeToken: TypeToken<*>) : VerifyError("No valid constructor found for $typeToken")

/**
 * Exception thrown when trying multiple fields/objects are bound to the same [DependencyKey] in a single Mockspresso instance.
 */
class RepeatedDependencyDefinedException(key: DependencyKey<*>) : VerifyError("Dependency defined multiple times in same Mockspresso instance, consider annotating with @Unmapped. DependencyKey: $key")

/**
 * Thrown when a special object maker returns an object that cannot be cast to the type represented by [key]
 */
class BrokenSpecialObjectMakerException(maker: SpecialObjectMaker, key: DependencyKey<*>, value: Any?) : IllegalArgumentException(
    "Special object maker returned an invalid object. SpecialObjectMaker: ${maker.javaClass.name}, expected: $key, but object returned type: ${value?.javaClass?.name}, value: $value")

class BrokenInjectionConfigException(config: InjectionConfig, token: TypeToken<*>, value: Any?) : IllegalArgumentException(
    "InjectionConfig returned an invalid constructor. InjectionConfig: ${config.javaClass.name}, expected: $token, but constructor resulted in object of type: ${value?.javaClass?.name}, value: $value")
