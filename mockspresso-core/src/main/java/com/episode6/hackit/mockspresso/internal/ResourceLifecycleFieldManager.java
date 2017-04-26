package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
class ResourceLifecycleFieldManager implements ResourcesLifecycleComponent {

  private final List<TestResource> mTestResources;
  private final MockerConfig mMockerConfig;
  private final DependencyMap mDependencyMap;
  private final DependencyProviderFactory mDependencyProviderFactory;
  private final DependencyMapImporter mDependencyMapImporter;
  private final RealObjectFieldTracker mRealObjectFieldTracker;

  public ResourceLifecycleFieldManager(
      Collection<TestResource> testResources,
      MockerConfig mockerConfig,
      DependencyMap dependencyMap,
      DependencyProviderFactory dependencyProviderFactory,
      DependencyMapImporter dependencyMapImporter,
      RealObjectFieldTracker realObjectFieldTracker) {
    mTestResources = new LinkedList<>(testResources);
    mMockerConfig = mockerConfig;
    mDependencyMap = dependencyMap;
    mDependencyProviderFactory = dependencyProviderFactory;
    mDependencyMapImporter = dependencyMapImporter;
    mRealObjectFieldTracker = realObjectFieldTracker;
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    MockerConfig.FieldPreparer mockFieldPreparer = mMockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mMockerConfig.provideMockAnnotations();
    for (TestResource resource : mTestResources) {
      Object o = resource.getObjectWithResources();

      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      // import mocks and non-null real objects into dependency map
      mDependencyMapImporter.importAnnotatedFields(o, mockAnnotations);
      mDependencyMapImporter.importAnnotatedFields(o, RealObject.class);

      // track down any @RealObjects that are null
      mRealObjectFieldTracker.scanNullRealObjectFields(o);
    }

    // since we haven't built any real objects yet, assert that we haven't
    // accidentally mapped a mock or other dependency to any of our RealObject keys
    mDependencyMap.assertDoesNotContainAny(mRealObjectFieldTracker.keySet());

    // fetch real object values from the dependencyProvider (now that they've been mapped)
    // and apply them to the fields found in realObjectFieldTracker
    for (DependencyKey key : mRealObjectFieldTracker.keySet()) {
      mRealObjectFieldTracker.applyValueToFields(key, mDependencyProviderFactory.getBlankDependencyProvider().get(key));
    }
  }

  @Override
  public void teardown() {
//TODO
  }
}
