package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to handle importing dependencies into a dependency map
 */
class FieldImporter {

  private final Set<Class<? extends Annotation>> mImportAnnotations;
  private final DependencyMap mDependencyMap;

  FieldImporter(
      Collection<Class<? extends Annotation>> importAnnotations,
      DependencyMap dependencyMap) {
    mImportAnnotations = new HashSet<>(importAnnotations);
    mDependencyMap = dependencyMap;
  }

  @SuppressWarnings("unchecked")
  void importAnnotatedFields(Object importFrom) {
    try {
      for (Field field : ReflectUtil.getAllDeclaredFields(importFrom.getClass())) {
        if (!ReflectUtil.isAnyAnnotationPresent(field, mImportAnnotations)) {
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
