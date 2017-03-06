package com.episode6.hackit.mockspresso.reflect;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Key used to identify objects and dependencies
 */
public final class DependencyKey<V> {

  public static DependencyKey<?> fromField(Field field) {
    return new DependencyKey<>(TypeToken.of(field), ReflectUtil.findQualifierAnnotation(field));
  }

  public final TypeToken<V> typeToken;
  public final @Nullable Annotation identityAnnotation;

  public DependencyKey(TypeToken<V> typeToken, @Nullable Annotation identityAnnotation) {
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
