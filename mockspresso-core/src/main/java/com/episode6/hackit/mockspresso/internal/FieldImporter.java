package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;

import org.jetbrains.annotations.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to handle importing dependencies into a dependency map
 */
class FieldImporter {

  interface KeyAdjuster {
    DependencyKey adjustKey(DependencyKey fieldKey, Field field);
  }

  private final Set<Class<? extends Annotation>> mImportAnnotations;
  private final DependencyMap mDependencyMap;
  private final KeyAdjuster mKeyAdjuster;

  FieldImporter(
      Collection<Class<? extends Annotation>> importAnnotations,
      DependencyMap dependencyMap,
      @Nullable KeyAdjuster keyAdjuster) {
    mImportAnnotations = new HashSet<>(importAnnotations);
    mDependencyMap = dependencyMap;
    mKeyAdjuster = keyAdjuster == null ? ((key, ignore) -> key) : keyAdjuster;
  }

  @SuppressWarnings("unchecked")
  void importAnnotatedFields(Object importFrom) {
    try {
      for (Field field : ReflectUtil.getAllDeclaredFields(importFrom.getClass())) {
        if (field.isAnnotationPresent(Unmapped.class) ||
            !ReflectUtil.isAnyAnnotationPresent(field, mImportAnnotations)) {
          continue;
        }

        field.setAccessible(true);
        Object fieldValue = field.get(importFrom);
        if (fieldValue != null) {
          DependencyKey key = mKeyAdjuster.adjustKey(DependencyKey.fromField(field), field);
          mDependencyMap.put(key, fieldValue, null);
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
