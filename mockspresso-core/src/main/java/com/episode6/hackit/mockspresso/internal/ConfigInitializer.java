package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoInitializer;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * Logic to initialize the last mile of a mockspresso config.
 * Includes field scanning & initializer calls
 */
public class ConfigInitializer {
  private final MockerConfig mMockerConfig;
  private final DependencyMap mDependencyMap;
  private final RealObjectMapping mRealObjectMapping;
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final List<Object> mObjectsWithFields;
  private final List<MockspressoInitializer> mInitializers;

  public ConfigInitializer(
      MockerConfig mockerConfig,
      DependencyMap dependencyMap,
      RealObjectMapping realObjectMapping,
      DependencyProviderFactory dependencyProviderFactory,
      List<Object> objectsWithFields,
      List<MockspressoInitializer> initializers) {
    mMockerConfig = mockerConfig;
    mDependencyMap = dependencyMap;
    mRealObjectMapping = realObjectMapping;
    mDependencyProviderFactory = dependencyProviderFactory;
    mObjectsWithFields = new LinkedList<>(objectsWithFields);
    mInitializers = new LinkedList<>(initializers);
  }

  public void init(Mockspresso mockspresso) {
    performFieldScanningAndInjection();
    callInitializers(mockspresso);
  }

  private void performFieldScanningAndInjection() {
    // prepare mObjectsWithFields
    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        mRealObjectMapping);
    DependencyMapImporter mDependencyMapImporter = new DependencyMapImporter(mDependencyMap);
    MockerConfig.FieldPreparer mockFieldPreparer = mMockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mMockerConfig.provideMockAnnotations();
    for (Object o : mObjectsWithFields) {
      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      // import mocks and non-null real objects into dependency map
      mDependencyMapImporter.importAnnotatedFields(o, mockAnnotations);
      mDependencyMapImporter.importAnnotatedFields(o, RealObject.class);

      // track down any @RealObjects that are null
      realObjectFieldTracker.scanNullRealObjectFields(o);
    }

    // since we haven't built any real objects yet, assert that we haven't
    // accidentally mapped a mock or other dependency to any of our RealObject keys
    mDependencyMap.assertDoesNotContainAny(realObjectFieldTracker.keySet());

    // fetch real object values from the dependencyProvider (now that they've been mapped)
    // and apply them to the fields found in realObjectFieldTracker
    for (DependencyKey key : realObjectFieldTracker.keySet()) {
      realObjectFieldTracker.applyValueToFields(key, mDependencyProviderFactory.getBlankDependencyProvider().get(key));
    }
  }

  private void callInitializers(Mockspresso instance) {
    for (MockspressoInitializer initializer : mInitializers) {
      initializer.setup(instance);
    }
  }
}
