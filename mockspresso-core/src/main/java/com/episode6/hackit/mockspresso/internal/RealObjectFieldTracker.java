package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.exception.RealObjectMappingMismatchException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * An internal class used to keep track of null fields annotated with @RealObject.
 *
 * To use, first pass all of your test objects (objects with annotated fields) into
 * {@link #scanNullRealObjectFields(Object)}. This will populate a map of all field-object pairs
 * that must be filled in, as well as add those mappings to the {@link RealObjectMapping} that
 * is passed to the constructor.
 *
 * Then you loop through {@link #keySet()}, create the objects and call
 * {@link #applyValueToFields(DependencyKey, Object)} to update the values of the fields
 */
public class RealObjectFieldTracker {

  private final HashMap<DependencyKey, Entry> mNullRealObjectFields = new HashMap<>();

  private final RealObjectMapping mRealObjectMapping;

  public RealObjectFieldTracker(RealObjectMapping realObjectMapping) {
    mRealObjectMapping = realObjectMapping;
  }

  public void scanNullRealObjectFields(Object object) {
    for (Field field : ReflectUtil.getAllDeclaredFields(object.getClass())) {
      if (!field.isAnnotationPresent(RealObject.class)) {
        continue;
      }

      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      trackFieldIfNull(field, object);
    }
  }

  public Set<DependencyKey> keySet() {
    return mNullRealObjectFields.keySet();
  }

  public void applyValueToFields(DependencyKey key, Object value) {
    if (!mNullRealObjectFields.containsKey(key)) {
      throw new RuntimeException(String.format("Could not find Key (%s) in tracked fields", key));
    }

    Entry entry = mNullRealObjectFields.get(key);
    entry.setValue(value);
  }

  private void trackFieldIfNull(Field field, Object object) {
    try {
      if (field.get(object) != null) {
        return;
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    DependencyKey key = DependencyKey.fromField(field);
    Entry entry = mNullRealObjectFields.get(key);
    if (entry == null) {
      entry = new Entry(field, object);
      mNullRealObjectFields.put(key, entry);
    } else {
      entry.add(field, object);
    }

    TypeToken implementationToken = entry.hasCustomImplementation() ?
        TypeToken.of(entry.implementationClass) :
        key.typeToken;
    mRealObjectMapping.put(key, implementationToken, true);
  }

  /**
   * An entry in our mNullRealObjectFields hashmap. Holds a collection of
   * Field/Object pairs and holds a ref to the implementation class to be used.
   * When adding new field/object pairs, the implementation class is always checked
   * and an exception thrown if there is a mis-match.
   */
  private static class Entry {
    final Class<?> implementationClass;
    final Collection<FieldInstance> fields;

    Entry(Field firstField, Object firstObject) {
      this.implementationClass = firstField.getAnnotation(RealObject.class).implementation();
      this.fields = new LinkedList<>();
      this.fields.add(new FieldInstance(firstField, firstObject));
    }

    void add(Field field, Object object) {
      Class<?> fieldImplementationClass = field.getAnnotation(RealObject.class).implementation();
      if (fieldImplementationClass != implementationClass) {
        throw new RealObjectMappingMismatchException(DependencyKey.fromField(field));
      }
      fields.add(new FieldInstance(field, object));
    }

    boolean hasCustomImplementation() {
      return implementationClass != RealObject.class;
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
