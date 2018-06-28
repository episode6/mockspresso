package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.Dependency;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.reflect.Field;

/**
 * Key adjuster for fields annotated with {@link Dependency}
 */
class DependencyFieldKeyAdjuster implements FieldImporter.KeyAdjuster {
  @Override
  public DependencyKey adjustKey(DependencyKey fieldKey, Field field) {
    if (!field.isAnnotationPresent(Dependency.class)) {
      return fieldKey;

    }
    Dependency dep = field.getAnnotation(Dependency.class);
    if (dep.bindAs() == Dependency.class) {
      return fieldKey;
    }
    return DependencyKey.of(dep.bindAs(), fieldKey.identityAnnotation);
  }
}
