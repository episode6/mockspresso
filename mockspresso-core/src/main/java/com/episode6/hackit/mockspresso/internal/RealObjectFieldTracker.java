package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.exception.RealObjectMappingMismatchException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.*;

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

  private final HashMap<DependencyKey, MappedFieldInfo> mMappedFields = new HashMap<>();
  private final List<UnmappedFieldInfo> mUnmappedFields = new LinkedList<>();

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
    for (Map.Entry<DependencyKey, MappedFieldInfo> entry : mMappedFields.entrySet()) {
      entry.getValue().setValue(blankDependencyProvider.get(entry.getKey()));
    }

    // apply values to unmapped fields
    for (UnmappedFieldInfo unmappedFieldInfo : mUnmappedFields) {
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
    for (MappedFieldInfo info : mMappedFields.values()) {
      info.setValue(null);
    }
    for (UnmappedFieldInfo info : mUnmappedFields) {
      info.setValue(null);
    }
    mMappedFields.clear();
    mUnmappedFields.clear();
    mRealObjectMapping.clear();
  }

  private void trackField(Field field, Object object) {
    if (field.isAnnotationPresent(Unmapped.class)) {
      mUnmappedFields.add(new UnmappedFieldInfo(field, object));
      return;
    }

    DependencyKey key = DependencyKey.fromField(field);
    MappedFieldInfo info = mMappedFields.get(key);
    if (info == null) {
      info = new MappedFieldInfo(field, object);
      mMappedFields.put(key, info);
    } else {
      info.add(field, object);
    }

    mRealObjectMapping.put(key, info.getImplementationToken(), true);
  }

  private abstract static class FieldInfo {
    final DependencyKey<?> dependencyKey;
    final Class<?> implementationClass;

    protected FieldInfo(Field field, Object object) {
      this.dependencyKey = DependencyKey.fromField(field);
      this.implementationClass = field.getAnnotation(RealObject.class).implementation();
    }

    boolean hasCustomImplementation() {
      return implementationClass != RealObject.class;
    }

    TypeToken<?> getImplementationToken() {
      return hasCustomImplementation() ?
          TypeToken.of(implementationClass) :
          dependencyKey.typeToken;
    }

    abstract void setValue(Object value);
  }

  private static class UnmappedFieldInfo extends FieldInfo {
    final FieldInstance field;

    protected UnmappedFieldInfo(Field field, Object object) {
      super(field, object);
      this.field = new FieldInstance(field, object);
    }

    @Override
    void setValue(Object value) {
      field.setValue(value);
    }
  }

  /**
   * An entry in our mMappedFields hashmap. Holds a collection of
   * Field/Object pairs and holds a ref to the implementation class to be used.
   * When adding new field/object pairs, the implementation class is always checked
   * and an exception thrown if there is a mis-match.
   */
  private static class MappedFieldInfo extends FieldInfo {
    final List<FieldInstance> fields;

    MappedFieldInfo(Field firstField, Object firstObject) {
      super(firstField, firstObject);
      this.fields = CollectionUtil.concatList(new FieldInstance(firstField, firstObject));
    }

    void add(Field field, Object object) {
      Class<?> fieldImplementationClass = field.getAnnotation(RealObject.class).implementation();
      if (fieldImplementationClass != implementationClass) {
        throw new RealObjectMappingMismatchException(DependencyKey.fromField(field));
      }
      fields.add(new FieldInstance(field, object));
    }

    void setValue(Object value) {
      for (FieldInstance fieldInstance : fields) {
        fieldInstance.setValue(value);
      }
    }
  }

  /**
   * An instance of a field and the object it belongs to.
   */
  private static class FieldInstance {
    final Field field;
    final Object object;

    FieldInstance(Field field, Object object) {
      this.field = field;
      this.object = object;
    }

    void setValue(Object value) {
      try {
        field.set(object, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
