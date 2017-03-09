package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.exception.MultipleQualifierAnnotationException;

import javax.annotation.Nullable;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Java reflect utils
 */
public class ReflectUtil {

  public static List<Field> getAllDeclaredFields(Class<?> clazz) {
    List<Field> fieldList = new LinkedList<>();
    fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
    if (clazz.getSuperclass() != null) {
      fieldList.addAll(getAllDeclaredFields(clazz.getSuperclass()));
    }
    return fieldList;
  }

  public static @Nullable Annotation findQualifierAnnotation(Field field) {
    return findQualifierAnnotation(field.getDeclaredAnnotations(), "field: " + field.getName());
  }

  public static @Nullable Annotation findQualifierAnnotation(Annotation[] annotations, String description) {
    Annotation found = null;
    for (Annotation annotation : annotations) {
      Qualifier qualifier = annotation.annotationType().getDeclaredAnnotation(Qualifier.class);
      if (qualifier == null) {
        continue;
      }
      if (found != null) {
        throw new MultipleQualifierAnnotationException(description);
      }
      found = annotation;
    }
    return found;
  }

  public static boolean isAnyAnnotationPresent(Field field, List<Class<? extends Annotation>> annotations) {
    for (Class<? extends Annotation> annotation : annotations) {
      if (field.isAnnotationPresent(annotation)) {
        return true;
      }
    }
    return false;
  }
}
