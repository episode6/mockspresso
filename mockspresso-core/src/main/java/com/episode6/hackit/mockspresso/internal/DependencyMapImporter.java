package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * Class to handle importing dependencies into a dependency map
 */
class DependencyMapImporter {

  private final DependencyMap mDependencyMap;

  DependencyMapImporter(DependencyMap dependencyMap) {
    mDependencyMap = dependencyMap;
  }

  void importAnnotatedFields(Object importFrom, Class<? extends Annotation> annotation) {
    importAnnotatedFields(importFrom, Collections.<Class<? extends Annotation>>singletonList(annotation));
  }

  @SuppressWarnings("unchecked")
  void importAnnotatedFields(Object importFrom, List<Class<? extends Annotation>> annotations) {
    try {
      for (Field field : ReflectUtil.getAllDeclaredFields(importFrom.getClass())) {
        if (!ReflectUtil.isAnyAnnotationPresent(field, annotations)) {
          continue;
        }

        field.setAccessible(true);
        Object fieldValue = field.get(importFrom);
        if (fieldValue != null) {
          DependencyKey key = DependencyKey.fromField(field);
          mDependencyMap.put(key, fieldValue, null);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
