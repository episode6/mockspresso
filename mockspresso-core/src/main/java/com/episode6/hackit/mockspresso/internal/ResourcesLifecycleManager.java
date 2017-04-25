package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Logic to initialize and tear-down the last mile of a mockspresso config.
 * Includes field scanning and initializer calls
 */
class ResourcesLifecycleManager {
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final Set<TestResource> mTestResources;
  private final ResourcesLifecycleMethodManager mResourcesLifecycleMethodManager;

  ResourcesLifecycleManager(
      DependencyProviderFactory dependencyProviderFactory,
      Set<TestResource> testResources) {
    mDependencyProviderFactory = dependencyProviderFactory;
    mTestResources = new LinkedHashSet<>(testResources);
    mResourcesLifecycleMethodManager = ResourcesLifecycleMethodManager.newInstance(mTestResources);
  }

  void setup(MockspressoInternal mockspresso) {
    MockspressoConfigContainer config = mockspresso.getConfig();
    performFieldScanningAndInjection(
        config.getMockerConfig(),
        config.getDependencyMap(),
        config.getRealObjectMapping());
    try {
      mResourcesLifecycleMethodManager.callBeforeMethods(mockspresso);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  void teardown() {
    try {
      mResourcesLifecycleMethodManager.callAfterMethods();
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private void performFieldScanningAndInjection(
      MockerConfig mockerConfig,
      DependencyMap dependencyMap,
      RealObjectMapping realObjectMapping) {
    // prepare mTestResources
    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        realObjectMapping);
    DependencyMapImporter mDependencyMapImporter = new DependencyMapImporter(dependencyMap);
    MockerConfig.FieldPreparer mockFieldPreparer = mockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mockerConfig.provideMockAnnotations();
    for (TestResource resource : mTestResources) {
      Object o = resource.getObjectWithResources();

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
}
