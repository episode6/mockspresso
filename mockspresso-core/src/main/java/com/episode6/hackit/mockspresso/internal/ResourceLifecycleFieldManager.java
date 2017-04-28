package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

import java.util.*;

/**
 * Handles the field scanning and injection of a mockspresso instance
 */
class ResourceLifecycleFieldManager implements ResourcesLifecycleComponent {

  private final List<TestResource> mTestResources;
  private final DependencyMap mDependencyMap;
  private final RealObjectMapping mRealObjectMapping;
  private final FieldImporter mFieldImporter;
  private final RealObjectFieldTracker mRealObjectFieldTracker;

  ResourceLifecycleFieldManager(
      Collection<TestResource> testResources,
      DependencyMap dependencyMap,
      RealObjectMapping realObjectMapping, FieldImporter fieldImporter,
      RealObjectFieldTracker realObjectFieldTracker) {
    mTestResources = new LinkedList<>(testResources);
    mDependencyMap = dependencyMap;
    mRealObjectMapping = realObjectMapping;
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

    // ensure that our dependency map and real objects dont intersect
    Set<DependencyKey> intersectionSet = new HashSet<>(mDependencyMap.keySet());
    intersectionSet.retainAll(mRealObjectMapping.keySet());
    if (!intersectionSet.isEmpty()) {
      throw new RepeatedDependencyDefinedException(intersectionSet.iterator().next());
    }

    // fetch real object values from the dependencyProvider (now that they've been mapped)
    // and apply them to the fields found in realObjectFieldTracker
    mRealObjectFieldTracker.applyValuesToFields();
  }

  @Override
  public void teardown() {
    mRealObjectFieldTracker.clear();
    mDependencyMap.clear();
    mRealObjectMapping.clear();
  }
}
