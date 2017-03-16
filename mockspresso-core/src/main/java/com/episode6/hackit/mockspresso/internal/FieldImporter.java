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
public class FieldImporter {

  private interface FieldHandler {
    void handleFoundField(Field field, Object fieldValue);
  }

  private final DependencyMap mDependencyMap;

  private final FieldHandler mNonNullFieldHandler;

  public FieldImporter(DependencyMap dependencyMap) {
    mDependencyMap = dependencyMap;
    mNonNullFieldHandler = new FieldHandler() {

      @SuppressWarnings("unchecked")
      @Override
      public void handleFoundField(Field field, Object fieldValue) {
        if (fieldValue != null) {
          DependencyKey key = DependencyKey.fromField(field);
          mDependencyMap.put(key, fieldValue);
        }
      }
    };
  }

  public void importNonNullFields(Object importFrom, Class<? extends Annotation> annotation) {
    importNonNullFields(importFrom, Collections.<Class<? extends Annotation>>singletonList(annotation));
  }

  public void importNonNullFields(Object importFrom, List<Class<? extends Annotation>> annotations) {
    scanFields(importFrom, annotations, mNonNullFieldHandler);
  }

  private void scanFields(Object from, List<Class<? extends Annotation>> annotations, FieldHandler fieldHandler) {
    try {
      for (Field field : ReflectUtil.getAllDeclaredFields(from.getClass())) {
        if (!ReflectUtil.isAnyAnnotationPresent(field, annotations)) {
          continue;
        }

        field.setAccessible(true);
        Object fieldValue = field.get(from);
        fieldHandler.handleFoundField(field, fieldValue);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
