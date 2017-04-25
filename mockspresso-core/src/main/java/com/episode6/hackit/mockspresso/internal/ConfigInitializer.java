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
 * Includes field scanning and initializer calls
 */
public class ConfigInitializer {
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final List<Object> mObjectsWithFields;
  private final List<MockspressoInitializer> mInitializers;

  public ConfigInitializer(
      DependencyProviderFactory dependencyProviderFactory,
      List<Object> objectsWithFields,
      List<MockspressoInitializer> initializers) {
    mDependencyProviderFactory = dependencyProviderFactory;
    mObjectsWithFields = new LinkedList<>(objectsWithFields);
    mInitializers = new LinkedList<>(initializers);
  }

  public void init(MockspressoInternal mockspresso) {
    MockspressoConfigContainer config = mockspresso.getConfig();
    performFieldScanningAndInjection(
        config.getMockerConfig(),
        config.getDependencyMap(),
        config.getRealObjectMapping());
    callInitializers(mockspresso);
  }

  private void performFieldScanningAndInjection(
      MockerConfig mockerConfig,
      DependencyMap dependencyMap,
      RealObjectMapping realObjectMapping) {
    // prepare mObjectsWithFields
    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        realObjectMapping);
    DependencyMapImporter mDependencyMapImporter = new DependencyMapImporter(dependencyMap);
    MockerConfig.FieldPreparer mockFieldPreparer = mockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mockerConfig.provideMockAnnotations();
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
    dependencyMap.assertDoesNotContainAny(realObjectFieldTracker.keySet());

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
