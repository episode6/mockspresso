package com.episode6.hackit.mockspresso.reflect;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Key used to identify objects and dependencies
 */
public final class DependencyKey<V> {

  public static <T> DependencyKey<T> of(Class<T> clazz) {
    return of(TypeToken.of(clazz));
  }

  public static <T> DependencyKey<T> of(TypeToken<T> typeToken) {
    return new DependencyKey<T>(typeToken, null);
  }

  public static <T> DependencyKey<T> of(Class<T> clazz, @Nullable Annotation identityAnnotation) {
    return of(TypeToken.of(clazz), identityAnnotation);
  }

  public static <T> DependencyKey<T> of(TypeToken<T> typeToken, @Nullable Annotation identityAnnotation) {
    return new DependencyKey<T>(typeToken, identityAnnotation);
  }

  public static DependencyKey<?> fromField(Field field) {
    return new DependencyKey<>(TypeToken.of(field.getGenericType()), ReflectUtil.findQualifierAnnotation(field));
  }

  public static DependencyKey<?> fromField(Field field, TypeToken<?> context) {
    return new DependencyKey<>(context.resolveType(field.getGenericType()), ReflectUtil.findQualifierAnnotation(field));
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

  @Override
  public String toString() {
    StringBuilder tsBuilder = new StringBuilder("DependencyKey{typeToken=")
        .append(typeToken);
    if (identityAnnotation != null) {
      tsBuilder.append(", identityAnnotation=").append(identityAnnotation);
    }
    return tsBuilder.append("}").toString();
  }
}
