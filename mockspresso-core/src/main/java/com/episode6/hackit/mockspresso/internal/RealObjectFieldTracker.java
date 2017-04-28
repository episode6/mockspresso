package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An internal class used to keep track of null fields annotated with @RealObject.
 *
 * To use, first pass all of your test objects (objects with annotated fields) into
 * {@link #scanNullRealObjectFields(Object)}. This will populate a map of all field-object pairs
 * that must be filled in, as well as add those mappings to the {@link RealObjectMapping} that
 * is passed to the constructor.
 *
 * Then you call {@link #applyValuesToFields()} which will loop through the map and
 * set the values on fields.
 */
class RealObjectFieldTracker {

  private final Map<DependencyKey, FieldInfo> mMappedFields = new HashMap<>();
  private final List<FieldInfo> mUnmappedFields = new LinkedList<>();

  private final RealObjectMapping mRealObjectMapping;
  private final RealObjectMaker mRealObjectMaker;
  private final DependencyProviderFactory mDependencyProviderFactory;

  RealObjectFieldTracker(
      RealObjectMapping realObjectMapping,
      RealObjectMaker realObjectMaker,
      DependencyProviderFactory dependencyProviderFacotry) {
    mRealObjectMapping = realObjectMapping;
    mRealObjectMaker = realObjectMaker;
    mDependencyProviderFactory = dependencyProviderFacotry;
  }

  void scanNullRealObjectFields(Object object) {
    for (Field field : ReflectUtil.getAllDeclaredFields(object.getClass())) {
      if (!field.isAnnotationPresent(RealObject.class)) {
        continue;
      }

      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      try {
        if (field.get(object) != null) {
          continue;
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }

      trackField(field, object);
    }
  }

  Set<DependencyKey> mappedKeys() {
    return mMappedFields.keySet();
  }

  @SuppressWarnings("unchecked")
  void applyValuesToFields() {
    // apply values to mapped fields
    DependencyProvider blankDependencyProvider = mDependencyProviderFactory.getBlankDependencyProvider();
    for (FieldInfo info : mMappedFields.values()) {
      info.setValue(blankDependencyProvider.get(info.dependencyKey));
    }

    // apply values to unmapped fields
    for (FieldInfo unmappedFieldInfo : mUnmappedFields) {
      DependencyProvider dependencyProvider =
          mDependencyProviderFactory.getDependencyProviderFor(unmappedFieldInfo.dependencyKey);
      Object value = mRealObjectMaker.createObject(
          dependencyProvider,
          unmappedFieldInfo.getImplementationToken());
      unmappedFieldInfo.setValue(value);
    }
  }

  /**
   * Nulls values for fields that were set & clears the backing RealObjectMapping
   */
  void clear() {
    for (FieldInfo info : mMappedFields.values()) {
      info.setValue(null);
    }
    for (FieldInfo info : mUnmappedFields) {
      info.setValue(null);
    }
    mMappedFields.clear();
    mUnmappedFields.clear();
    mRealObjectMapping.clear();
  }

  private void trackField(Field field, Object object) {
    FieldInfo info = new FieldInfo(field, object);
    if (field.isAnnotationPresent(Unmapped.class)) {
      mUnmappedFields.add(info);
      return;
    }

    if (mMappedFields.put(info.dependencyKey, info) != null) {
      throw new RepeatedDependencyDefinedException(info.dependencyKey);
    }
    mRealObjectMapping.put(info.dependencyKey, info.getImplementationToken(), true);
  }

  private static class FieldInfo {
    final DependencyKey dependencyKey;
    final Class<?> implementationClass;
    final Field field;
    final Object object;

    FieldInfo(Field field, Object object) {
      this.dependencyKey = DependencyKey.fromField(field);
      this.implementationClass = field.getAnnotation(RealObject.class).implementation();
      this.field = field;
      this.object = object;
    }

    boolean hasCustomImplementation() {
      return implementationClass != RealObject.class;
    }

    TypeToken<?> getImplementationToken() {
      return hasCustomImplementation() ?
          TypeToken.of(implementationClass) :
          dependencyKey.typeToken;
    }

    void setValue(Object value) {
      try {
        field.set(object, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      FieldInfo fieldInfo = (FieldInfo) o;

      return dependencyKey.equals(fieldInfo.dependencyKey);
    }

    @Override
    public int hashCode() {
      return dependencyKey.hashCode();
    }
  }
}
