package com.episode6.hackit.mockspresso.exception

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
