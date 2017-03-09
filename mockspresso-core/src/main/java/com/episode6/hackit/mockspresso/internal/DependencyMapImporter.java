package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to handle importing dependencies into a dependency map
 */
public class DependencyMapImporter {

  private final DependencyMap mDependencyMap;

  public DependencyMapImporter(DependencyMap dependencyMap) {
    mDependencyMap = dependencyMap;
  }

  public DependencyMapImporter annotatedFields(Object importFrom, Class<? extends Annotation> annotation) {
    return annotatedFields(importFrom, Collections.<Class<? extends Annotation>>singletonList(annotation));
  }

  @SuppressWarnings("unchecked")
  public DependencyMapImporter annotatedFields(Object importFrom, List<Class<? extends Annotation>> annotations) {
    try {
      for (Field field : getAllFields(importFrom.getClass())) {
        if (!fieldHasAnnotation(field, annotations)) {
          continue;
        }

        field.setAccessible(true);
        Object fieldValue = field.get(importFrom);
        if (fieldValue != null) {
          DependencyKey key = DependencyKey.fromField(field);
          mDependencyMap.put(key, fieldValue);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  private static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fieldList = new LinkedList<>();
    fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
    if (clazz.getSuperclass() != null) {
      fieldList.addAll(getAllFields(clazz.getSuperclass()));
    }
    return fieldList;
  }

  private static boolean fieldHasAnnotation(Field field, List<Class<? extends Annotation>> annotations) {
    for (Class<? extends Annotation> annotation : annotations) {
      if (field.isAnnotationPresent(annotation)) {
        return true;
      }
    }
    return false;
  }
}
