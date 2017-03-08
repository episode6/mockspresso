package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

  public DependencyMapImporter annotatedFields(Object importFrom, List<Class<? extends Annotation>> annotations) {
    try {
      for (Field field : importFrom.getClass().getDeclaredFields()) {
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

  @SuppressWarnings("unchecked")
  public DependencyMapImporter dependencyMap(DependencyMap otherMap) {
    for (Map.Entry<DependencyKey, Object> entry : otherMap.entrySet()) {
      mDependencyMap.put(entry.getKey(), entry.getValue());
    }
    return this;
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
