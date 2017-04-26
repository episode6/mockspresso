package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.DependencyProvider;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
class ResourceLifecycleFieldManager implements ResourcesLifecycleComponent {

  private final List<TestResource> mTestResources;
  private final List<Class<? extends Annotation>> mMockAnnotations;
  private final DependencyMap mDependencyMap;
  private final DependencyMapImporter mDependencyMapImporter;
  private final RealObjectFieldTracker mRealObjectFieldTracker;

  public ResourceLifecycleFieldManager(
      Collection<TestResource> testResources,
      List<Class<? extends Annotation>> mockAnnotations,
      DependencyMap dependencyMap,
      DependencyMapImporter dependencyMapImporter,
      RealObjectFieldTracker realObjectFieldTracker) {
    mTestResources = new LinkedList<>(testResources);
    mMockAnnotations = new LinkedList<>(mockAnnotations);
    mDependencyMap = dependencyMap;
    mDependencyMapImporter = dependencyMapImporter;
    mRealObjectFieldTracker = realObjectFieldTracker;
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    for (TestResource resource : mTestResources) {
      Object o = resource.getObjectWithResources();

      // import mocks and non-null real objects into dependency map
      mDependencyMapImporter.importAnnotatedFields(o, mMockAnnotations);
      mDependencyMapImporter.importAnnotatedFields(o, RealObject.class);

      // track down any @RealObjects that are null
      mRealObjectFieldTracker.scanNullRealObjectFields(o);
    }

    // since we haven't built any real objects yet, assert that we haven't
    // accidentally mapped a mock or other dependency to any of our RealObject keys
    mDependencyMap.assertDoesNotContainAny(mRealObjectFieldTracker.keySet());

    // fetch real object values from the dependencyProvider (now that they've been mapped)
    // and apply them to the fields found in realObjectFieldTracker
    mRealObjectFieldTracker.applyValuesToFields();
  }

  @Override
  public void teardown() {
    mRealObjectFieldTracker.clear();
    mDependencyMap.clear();
  }
}
