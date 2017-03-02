package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Key used to identify objects and dependencies
 */
public final class DependencyKey<V> {

  public static <T> DependencyKey<T> from(Class<T> clazz) {
    return from(TypeToken.of(clazz), null);
  }

  public static <T> DependencyKey<T> from(Class<T> clazz, @Nullable Annotation identityAnnotation) {
    return from(TypeToken.of(clazz), identityAnnotation);
  }

  public static <T> DependencyKey<T> from(TypeToken<T> typeToken) {
    return from(typeToken, null);
  }

  public static <T> DependencyKey<T> from(TypeToken<T> typeToken, @Nullable Annotation identityAnnotation) {
    return new DependencyKey<T>(typeToken, identityAnnotation);
  }

  public static DependencyKey<?> from(Field field) {
    return from(TypeToken.of(field), ReflectUtil.findQualifierAnnotation(field));
  }

  public final TypeToken<V> typeToken;
  public final @Nullable Annotation identityAnnotation;

  private DependencyKey(TypeToken<V> typeToken, @Nullable Annotation identityAnnotation) {
    this.typeToken = typeToken;
    this.identityAnnotation = identityAnnotation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DependencyKey<?> that = (DependencyKey<?>) o;

    if (!typeToken.equals(that.typeToken)) return false;
    return identityAnnotation != null ? identityAnnotation.equals(that.identityAnnotation) : that.identityAnnotation == null;
  }

  @Override
  public int hashCode() {
    int result = typeToken.hashCode();
    result = 31 * result + (identityAnnotation != null ? identityAnnotation.hashCode() : 0);
    return result;
  }
}
