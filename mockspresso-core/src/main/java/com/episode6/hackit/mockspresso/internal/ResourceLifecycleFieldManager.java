package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles the field scanning and injection of a mockspresso instance
 */
class ResourceLifecycleFieldManager implements ResourcesLifecycleComponent {

  private final List<TestResource> mTestResources;
  private final DependencyMap mDependencyMap;
  private final FieldImporter mFieldImporter;
  private final RealObjectFieldTracker mRealObjectFieldTracker;

  ResourceLifecycleFieldManager(
      Collection<TestResource> testResources,
      DependencyMap dependencyMap,
      FieldImporter fieldImporter,
      RealObjectFieldTracker realObjectFieldTracker) {
    mTestResources = new LinkedList<>(testResources);
    mDependencyMap = dependencyMap;
    mFieldImporter = fieldImporter;
    mRealObjectFieldTracker = realObjectFieldTracker;
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    for (TestResource resource : mTestResources) {
      Object o = resource.getObjectWithResources();

      // import mocks and non-null real objects into dependency map
      mFieldImporter.importAnnotatedFields(o);

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
