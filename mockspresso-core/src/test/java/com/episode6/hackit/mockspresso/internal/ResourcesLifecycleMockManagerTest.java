package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests {@link ResourcesLifecycleMockManager}
 */
@RunWith(DefaultTestRunner.class)
public class ResourcesLifecycleMockManagerTest {

  @Mock Mockspresso mMockspresso;
  @Mock MockerConfig.FieldPreparer mFieldPreparer;

  List<TestResource> mTestResources;
  ResourcesLifecycleMockManager mResourcesLifecycleMockManager;

  AutoCloseable mockitoClosable;

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);

    mTestResources = new ArrayList<>(3);
    mTestResources.add(new TestResource(new Object(), false));
    mTestResources.add(new TestResource(new Object(), true));
    mTestResources.add(new TestResource(new Object(), false));

    mResourcesLifecycleMockManager = new ResourcesLifecycleMockManager(mTestResources, mFieldPreparer);
  }

  @Test
  public void testPreparationOrder() {
    mResourcesLifecycleMockManager.setup(mMockspresso);

    InOrder inOrder = Mockito.inOrder(mFieldPreparer);
    for (int i = 0; i < mTestResources.size(); i++) {
      inOrder.verify(mFieldPreparer).prepareFields(mTestResources.get(i).getObjectWithResources());
    }
    verifyNoMoreInteractions(mFieldPreparer);
  }
}
