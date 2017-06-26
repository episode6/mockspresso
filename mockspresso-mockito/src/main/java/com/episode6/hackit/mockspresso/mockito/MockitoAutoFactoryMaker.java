package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of {@link SpecialObjectMaker} that creates mock factories which in-turn
 * query the dependencyProvider for a real binding when their methods are called.
 *
 * Mockito is used to mock the factory classes with a default answer. Upon invocation, that
 * default answer will query the dependencyProvider for a key based on the invoked method's
 * return type (and optional qualifier annotation).
 *
 * This class should support most generic factory types.
 */
public class MockitoAutoFactoryMaker implements SpecialObjectMaker {

  public static MockitoAutoFactoryMaker create(Class<?>... classes) {
    return new MockitoAutoFactoryMaker(Arrays.asList(classes));
  }

  private final List<Class<?>> mClasses;

  public MockitoAutoFactoryMaker(List<Class<?>> classes) {
    mClasses = classes;
  }

  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    return mClasses.contains(key.typeToken.getRawType());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key) {
    if (!canMakeObject(key)) {
      return null;
    }

    return Mockito.mock((Class<T>)key.typeToken.getRawType(), new FactoryAnswer(key, dependencyProvider));
  }

  private static class FactoryAnswer implements Answer<Object> {
    private final DependencyKey<?> mOriginalKey;
    private final DependencyProvider mDependencyProvider;

    private FactoryAnswer(
        DependencyKey<?> originalKey,
        DependencyProvider dependencyProvider) {
      mOriginalKey = originalKey;
      mDependencyProvider = dependencyProvider;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      Method method = invocation.getMethod();
      if (method.getReturnType().equals(Void.TYPE)) {
        return null;
      }

      Type genericReturnType = method.getGenericReturnType();
      if (genericReturnType instanceof TypeVariable) {
        try {
          genericReturnType = findActualTypeOfGenericTypeArgument(
              mOriginalKey.typeToken,
              method.getDeclaringClass(),
              ((TypeVariable) genericReturnType).getName());
        } catch (Throwable t) {
          throw new AssertionError(
              "Could not determine return type for automatic factory class: " +
                  mOriginalKey.typeToken + ", method: " + method.getName(),
              t);
        }
      }

      TypeToken<?> returnType = TypeToken.of(genericReturnType);
      @Nullable Annotation annotation = ReflectUtil.findQualifierAnnotation(method);
      DependencyKey<?> subKey = DependencyKey.of(returnType, annotation);
      return mDependencyProvider.get(subKey);
    }
  }

  /**
   * Find the actual type of the generic type param named rootTypeParamName on rootClass, for
   * the given factoryToken (which is assumed to be an implementor of rootClass)
   * @param factoryToken The fully-formed type-token for an implementation of rootClass
   * @param rootClass The root class that has a generic type argument named rootTypeParamName
   * @param rootTypeParamName The name of the generic type argument (declared on rootClass) that
   *                      we need to actual type for.
   * @return The actual type that factoryToken uses for rootClass's rootTypeParamName
   */
  private static Type findActualTypeOfGenericTypeArgument(
      TypeToken<?> factoryToken,
      Class<?> rootClass,
      String rootTypeParamName) {
    if (!(factoryToken.getType() instanceof ParameterizedType)) {
      throw new IllegalArgumentException("Expected " + factoryToken.getType() + " to be a ParameterizedType");
    }

    int typeArgumentIndex = findIndexForTypeArgument(factoryToken.getRawType(), rootClass, rootTypeParamName);
    return ((ParameterizedType)factoryToken.getType()).getActualTypeArguments()[typeArgumentIndex];
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
  private static int findIndexForTypeArgument(Class<?> implClass, Class<?> rootClass, String rootTypeParamName) {
    if (implClass == rootClass) {
      return findIndexForTypeArgument(implClass, rootTypeParamName);
    }

    if (!rootClass.isAssignableFrom(implClass)) {
      throw new IllegalArgumentException(rootClass + " is not assignable from " + implClass);
    }

    Class<?> superclass = implClass.getSuperclass();
    if (superclass != null && superclass != Object.class && rootClass.isAssignableFrom(superclass)) {
      int superclassIndex = findIndexForTypeArgument(superclass, rootClass, rootTypeParamName);
      String localTypeName = getNameOfTypeVariable(
          (ParameterizedType) implClass.getGenericSuperclass(),
          superclassIndex);
      return findIndexForTypeArgument(implClass, localTypeName);
    }

    Class<?>[] interfaces = implClass.getInterfaces();
    Type[] genericInterfaces = implClass.getGenericInterfaces();
    for (int i = 0; i< interfaces.length; i++) {
      if (rootClass.isAssignableFrom(interfaces[i])) {
        int interfaceIndex = findIndexForTypeArgument(interfaces[i], rootClass, rootTypeParamName);
        String localTypeName = getNameOfTypeVariable(
            (ParameterizedType) genericInterfaces[i],
            interfaceIndex);
        return findIndexForTypeArgument(implClass, localTypeName);
      }
    }

    throw new IllegalArgumentException("Could not find connection between " + implClass + " and " + rootClass);
  }

  /**
   * Find the index for the type argument named typeParamName in clazz's type parameters.
   * @param clazz A generic class with type parameters
   * @param typeParamName The name of the type parameter we want the index of
   * @return The index of the type argument named typeParamName in clazz's type parameters.
   */
  private static int findIndexForTypeArgument(Class<?> clazz, String typeParamName) {
    TypeVariable[] typeVariables = clazz.getTypeParameters();
    for (int i = 0; i<typeVariables.length; i++) {
      if (typeVariables[i].getName().equals(typeParamName)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Could not find type param named " + typeParamName + " on class " + clazz);
  }

  /**
   * Get the name of a TypeVariable in parameterizedType's type arguments
   * @param parameterizedType The ParameterizedType with TypeVarables for arguments
   * @param typeArgIndex The index of the argument we want the name of.
   * @return The name of parameterizedType.getActualTypeArgument()[index]
   */
  private static String getNameOfTypeVariable(ParameterizedType parameterizedType, int typeArgIndex) {
    Type arg = parameterizedType.getActualTypeArguments()[typeArgIndex];
    if (!(arg instanceof TypeVariable)) {
      throw new IllegalArgumentException("Expected a type variable when looking at arg " + arg + "on class " + parameterizedType);
    }
    return ((TypeVariable) arg).getName();
  }
}
