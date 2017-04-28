package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockerConfig;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * ResourcesLifecycleComponent that passes on mock field preparation to the provided
 * {@link MockerConfig.FieldPreparer}
 */
class ResourcesLifecycleMockManager implements ResourcesLifecycleComponent {

  private final List<TestResource> mTestResources;
  private final MockerConfig.FieldPreparer mFieldPreparer;

  ResourcesLifecycleMockManager(
      Collection<TestResource> testResources,
      MockerConfig.FieldPreparer fieldPreparer) {
    mTestResources = new LinkedList<>(testResources);
    mFieldPreparer = fieldPreparer;
  }

  @Override
  public void setup(Mockspresso mockspresso) {
    for (TestResource resource : mTestResources) {
      mFieldPreparer.prepareFields(resource.getObjectWithResources());
    }
  }

  @Override
  public void teardown() {
    // We currently have no teardown step for mocks...
  }
}
