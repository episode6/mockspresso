package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.ReflectUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * An internal class used to keep track of, and fill in, null fields annotated with @RealObject.
 *
 * To use, first pass all of your test objects (objects with annotated fields) into
 * {@link #scanNullRealObjectFields(Object)}. This will populate a map of all field-object pairs
 * that must be filled in.
 *
 * After all test objects have been scanned, call {@link #createAndAssignTrackedRealObjects()}.
 * The provided {@link RealObjectMaker} will be used to create all of the missing @RealObjects. If
 * One @RealObject depends on another @RealObject, they will be created in-order.
 */
public class RealObjectFieldTracker {

  private final HashMap<DependencyKey, Collection<FieldInstance>> mNullRealObjectFields = new HashMap<>();

  private final RealObjectMaker mRealObjectMaker;
  private final DependencyMap mDependencyMap;
  private final CustomDependencyProvider mCustomDependencyProvider;

  /**
   * We require the full {@link MockspressoConfigContainer} here to ensure the DependencyProvider
   * we work with is attached to the DependencyMap we're populating.
   */
  public RealObjectFieldTracker(MockspressoConfigContainer configContainer) {
    mRealObjectMaker = configContainer.getRealObjectMaker();
    mDependencyMap = configContainer.getDependencyMap();
    mCustomDependencyProvider = new CustomDependencyProvider(configContainer.getDependencyProvider());
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

  public void createAndAssignTrackedRealObjects() {
    // create a copy of the keyset, since the contents of mNullRealObjectFields
    // will change unexpectedly depending on the order of dependencies.
    Set<DependencyKey> keySet = new HashSet<>(mNullRealObjectFields.keySet());
    for (DependencyKey key : keySet) {
      createAndAssignObjectIfNeeded(key);
    }
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
    Collection<FieldInstance> fieldCollection = mNullRealObjectFields.get(key);
    if (fieldCollection == null) {
      fieldCollection = new LinkedList<>();
      mNullRealObjectFields.put(key, fieldCollection);
    }
    fieldCollection.add(new FieldInstance(field, object));
  }

  @SuppressWarnings("unchecked")
  private void createAndAssignObjectIfNeeded(DependencyKey key) {
    Collection<FieldInstance> fields = mNullRealObjectFields.remove(key);
    if (fields == null) {
      // dependency has already been handled
      return;
    }

    Object value = mRealObjectMaker.createObject(mCustomDependencyProvider, key.typeToken);
    mDependencyMap.put(key, value);
    for (FieldInstance field : fields) {
      field.setValue(value);
    }
  }

  /**
   * A custom implementation of DependencyProvider. We use this to
   * ensure we check our internal map of null @RealObject dependencies
   * before returning from the provided DependencyProvider;
   */
  private class CustomDependencyProvider implements DependencyProvider {

    private final DependencyProvider mDependencyProvider;

    CustomDependencyProvider(DependencyProvider dependencyProvider) {
      mDependencyProvider = dependencyProvider;
    }

    @Override
    public <T> T get(DependencyKey<T> key) {
      createAndAssignObjectIfNeeded(key);
      return mDependencyProvider.get(key);
    }
  }

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
