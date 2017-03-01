package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.exception.MultipleQualifierAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Java reflect utils
 */
public class ReflectUtil {

  public static @Nullable Annotation findQualifierAnnotation(Field field) {
    Annotation found = null;
    for (Annotation annotation : field.getDeclaredAnnotations()) {
      Qualifier qualifier = annotation.annotationType().getDeclaredAnnotation(Qualifier.class);
      if (qualifier == null) {
        continue;
      }
      if (found != null) {
        throw new MultipleQualifierAnnotationException(field);
      }
      found = annotation;
    }
    return found;
  }
}
