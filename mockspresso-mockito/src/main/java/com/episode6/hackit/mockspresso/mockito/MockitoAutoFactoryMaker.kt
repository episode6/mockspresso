package com.episode6.hackit.mockspresso.mockito

import com.episode6.hackit.mockspresso.api.DependencyProvider
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * An implementation of [SpecialObjectMaker] that creates mock factories which in-turn
 * query the dependencyProvider for a real binding when their methods are called.
 *
 * Mockito is used to mock the factory classes with a default answer. Upon invocation, that
 * default answer will query the dependencyProvider for a key based on the invoked method's
 * return type (and the factory's optional qualifier annotation).
 *
 * This class should support most generic factory types.
 */
internal class MockitoAutoFactoryMaker(private val classes: List<Class<*>>) : SpecialObjectMaker {
  companion object {
    @JvmStatic fun create(vararg classes: Class<*>) = MockitoAutoFactoryMaker(classes.toList())
  }

  override fun canMakeObject(key: DependencyKey<*>): Boolean = classes.contains(key.typeToken.rawType)

  override fun makeObject(dependencyProvider: DependencyProvider, key: DependencyKey<*>): Any? {
    if (!canMakeObject(key)) return null
    return Mockito.mock(key.typeToken.rawType, FactoryAnswer(key, dependencyProvider))
  }
}

private class FactoryAnswer constructor(
    private val originalKey: DependencyKey<*>,
    private val dependencyProvider: DependencyProvider) : Answer<Any> {

  override fun answer(invocation: InvocationOnMock): Any? {
    if (invocation.method.returnType == Void.TYPE) {
      return null
    }
    val genericReturnType = when (val genRtrType = invocation.method.genericReturnType) {
      !is TypeVariable<*> -> genRtrType
      else                -> findActualTypeOfGenericTypeArgument(
          factoryToken = originalKey.typeToken,
          rootClass = invocation.method.declaringClass,
          rootTypeParamName = genRtrType.name,
          errorProvider = { "Could not determine return type for automatic factory class: ${originalKey.typeToken} , method: ${invocation.method.name}" })
    }
    val subKey = DependencyKey.of(TypeToken.of(genericReturnType), originalKey.identityAnnotation)
    return dependencyProvider[subKey]
  }
}


/**
 * Find the actual type of the generic type param named rootTypeParamName on rootClass, for
 * the given factoryToken (which is assumed to be an implementor of rootClass)
 * @param factoryToken The fully-formed type-token for an implementation of rootClass
 * @param rootClass The root class that has a generic type argument named rootTypeParamName
 * @param rootTypeParamName The name of the generic type argument (declared on rootClass) that
 * we need to actual type for.
 * @return The actual type that factoryToken uses for rootClass's rootTypeParamName
 */
private fun findActualTypeOfGenericTypeArgument(
    factoryToken: TypeToken<*>,
    rootClass: Class<*>,
    rootTypeParamName: String,
    errorProvider: () -> String): Type {
  try {
    require(factoryToken.type is ParameterizedType) { "Expected ${factoryToken.type} to be a ParameterizedType" }
    val typeArgumentIndex = findIndexForTypeArgument(factoryToken.rawType, rootClass, rootTypeParamName)
    return (factoryToken.type as ParameterizedType).actualTypeArguments[typeArgumentIndex]
  } catch (t: Throwable) {
    throw AssertionError(errorProvider(), t)
  }
}

/**
 * Given: implClass is an implementation of rootClass. rootClass declares a generic type argument
 * name rootTypeParamName. implClass also declares this type argument generically (but we don't know
 * its name).
 * Find the index, in implClass's type arguments, of the argument that represents rootClass's
 * rootTypeParamName argument.
 * @param implClass The class that implements rootClass
 * @param rootClass The class that has a generic type argument name rootTypeParamName
 * @param rootTypeParamName The type argument on rootClass that we need to map to implClass's type arguments
 * @return the index, in implClass's type arguments, of the argument that represents rootClass's
 * rootTypeParamName argument.
 */
private fun findIndexForTypeArgument(implClass: Class<*>, rootClass: Class<*>, rootTypeParamName: String): Int {
  if (implClass == rootClass) {
    return findIndexForTypeArgument(implClass, rootTypeParamName)
  }

  require(rootClass.isAssignableFrom(implClass)) { "$rootClass is not assignable from $implClass" }
  val superclass = implClass.superclass
  if (superclass != null && superclass != Any::class.java && rootClass.isAssignableFrom(superclass)) {
    val superclassIndex = findIndexForTypeArgument(superclass, rootClass, rootTypeParamName)
    val localTypeName = getNameOfTypeVariable(implClass.genericSuperclass as ParameterizedType, superclassIndex)
    return findIndexForTypeArgument(implClass, localTypeName)
  }

  val interfaces = implClass.interfaces
  val genericInterfaces = implClass.genericInterfaces
  val declarationIndex = interfaces.indexOfFirst { rootClass.isAssignableFrom(it) }.takeIf { it >= 0 }
      ?: throw IllegalArgumentException("Could not find connection between $implClass and $rootClass")

  val typeArgumentIndex = findIndexForTypeArgument(interfaces[declarationIndex], rootClass, rootTypeParamName)
  val localTypeName = getNameOfTypeVariable(genericInterfaces[declarationIndex] as ParameterizedType, typeArgumentIndex)
  return findIndexForTypeArgument(implClass, localTypeName)

}

/**
 * Find the index for the type argument named typeParamName in clazz's type parameters.
 * @param clazz A generic class with type parameters
 * @param typeParamName The name of the type parameter we want the index of
 * @return The index of the type argument named typeParamName in clazz's type parameters.
 */
private fun findIndexForTypeArgument(clazz: Class<*>, typeParamName: String): Int = clazz.typeParameters
    .indexOfFirst { it.name == typeParamName }
    .takeIf { it >= 0 }
    ?: throw IllegalArgumentException("Could not find type param named $typeParamName on class $clazz")

/**
 * Get the name of a TypeVariable in parameterizedType's type arguments
 * @param parameterizedType The ParameterizedType with TypeVarables for arguments
 * @param typeArgIndex The index of the argument we want the name of.
 * @return The name of parameterizedType.getActualTypeArgument()[index]
 */
private fun getNameOfTypeVariable(parameterizedType: ParameterizedType, typeArgIndex: Int): String {
  val arg = parameterizedType.actualTypeArguments[typeArgIndex]
  require(arg is TypeVariable<*>) { "Expected a type variable when looking at arg $arg on class $parameterizedType" }
  return arg.name
}

