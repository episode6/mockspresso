/*
 * Copied from Gson's old TypeToken implementation from:
 * https://github.com/google/gson/blob/3b0f8f4340c68d8fde0840befbc1b89afa14933f/gson/src/main/java/com/google/gson/reflect/TypeToken.java
 *
 * Removed some unneeded methods, and their associated comments, but left all other comments in-tact
 * Original licence included below:
 *
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.episode6.hackit.mockspresso.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents a generic type {@code T}.
 *
 * You can use this class to get the generic type for a class. For example,
 * to get the generic type for <code>Collection&lt;Foo&gt;</code>, you can use:
 * <p>
 * <code>Type typeOfCollectionOfFoo = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType()
 * </code>
 *
 * <p>Assumes {@code Type} implements {@code equals()} and {@code hashCode()}
 * as a value (as opposed to identity) comparison.
 *
 *
 * @author Bob Lee
 * @author Sven Mawson
 */
public abstract class TypeToken<T> {

  final Class<? super T> rawType;
  final Type type;

  /**
   * Constructs a new type token. Derives represented class from type
   * parameter.
   *
   * <p>Clients create an empty anonymous subclass. Doing so embeds the type
   * parameter in the anonymous class's type hierarchy so we can reconstitute
   * it at runtime despite erasure.
   *
   * <p>For example:
   * <code>
   * {@literal TypeToken<List<String>> t = new TypeToken<List<String>>}(){}
   * </code>
   */
  @SuppressWarnings("unchecked")
  protected TypeToken() {
    this.type = getSuperclassTypeParameter(getClass());
    this.rawType = (Class<? super T>) getRawType(type);
  }

  /**
   * Unsafe. Constructs a type token manually.
   */
  @SuppressWarnings({"unchecked"})
  private TypeToken(Type type) {
    this.rawType = (Class<? super T>) getRawType(nonNull(type, "type"));
    this.type = type;
  }

  private static <T> T nonNull(T o, String message) {
    if (o == null) {
      throw new NullPointerException(message);
    }
    return o;
  }

  /**
   * Gets type from super class's type parameter.
   */
  private static Type getSuperclassTypeParameter(Class<?> subclass) {
    Type superclass = subclass.getGenericSuperclass();
    if (superclass instanceof Class<?>) {
      throw new RuntimeException("Missing type parameter.");
    }
    return ((ParameterizedType) superclass).getActualTypeArguments()[0];
  }

  private static Class<?> getRawType(Type type) {
    if (type instanceof Class<?>) {
      // type is a normal class.
      return (Class<?>) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      // I'm not exactly sure why getRawType() returns Type instead of Class.
      // Neal isn't either but suspects some pathological case related
      // to nested classes exists.
      Type rawType = parameterizedType.getRawType();
      if (rawType instanceof Class<?>) {
        return (Class<?>) rawType;
      }
      throw buildUnexpectedTypeError(rawType, Class.class);
    } else if (type instanceof GenericArrayType) {
      GenericArrayType genericArrayType = (GenericArrayType) type;

      // TODO(jleitch): This is not the most efficient way to handle generic
      // arrays, but is there another way to extract the array class in a
      // non-hacky way (i.e. using String value class names- "[L...")?
      Object rawArrayType = Array.newInstance(
          getRawType(genericArrayType.getGenericComponentType()), 0);
      return rawArrayType.getClass();
    } else {
      throw buildUnexpectedTypeError(
          type, ParameterizedType.class, GenericArrayType.class);
    }
  }

  /**
   * Gets the raw type.
   */
  public Class<? super T> getRawType() {
    return rawType;
  }

  /**
   * Gets underlying {@code Type} instance.
   */
  public Type getType() {
    return type;
  }

  /**
   * Hashcode for this object.
   * @return hashcode for this object.
   */
  @Override public int hashCode() {
    return type.hashCode();
  }

  /**
   * Method to test equality.
   *
   * @return true if this object is logically equal to the specified object, false otherwise.
   */
  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof TypeToken<?>)) {
      return false;
    }
    TypeToken<?> t = (TypeToken<?>) o;
    return type.equals(t.type);
  }

  /**
   * Returns a string representation of this object.
   * @return a string representation of this object.
   */
  @Override public String toString() {
    return type instanceof Class<?>
        ? ((Class<?>) type).getName()
        : type.toString();
  }

  private static AssertionError buildUnexpectedTypeError(
      Type token, Class<?>... expected) {

    // Build exception message
    StringBuilder exceptionMessage =
        new StringBuilder("Unexpected type. Expected one of: ");
    for (Class<?> clazz : expected) {
      exceptionMessage.append(clazz.getName()).append(", ");
    }
    exceptionMessage.append("but got: ").append(token.getClass().getName())
        .append(", for type token: ").append(token.toString()).append('.');

    return new AssertionError(exceptionMessage.toString());
  }

  /**
   * Gets type token for the given {@code Type} instance.
   */
  public static TypeToken<?> of(Type type) {
    return new SimpleTypeToken<Object>(type);
  }

  /**
   * Gets type token for the given {@code Class} instance.
   */
  public static <T> TypeToken<T> of(Class<T> type) {
    return new SimpleTypeToken<T>(type);
  }

  /**
   * Private static class to not create more anonymous classes than
   * necessary.
   */
  private static class SimpleTypeToken<T> extends TypeToken<T> {
    public SimpleTypeToken(Type type) {
      super(type);
    }
  }
}