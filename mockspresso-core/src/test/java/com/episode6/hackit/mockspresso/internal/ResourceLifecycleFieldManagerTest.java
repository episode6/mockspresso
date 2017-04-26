package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests {@link ResourceLifecycleFieldManager}
 */
@RunWith(DefaultTestRunner.class)
public class ResourceLifecycleFieldManagerTest {

  @Mock Mockspresso mMockspresso;

  @Mock DependencyMap mDependencyMap;
  @Mock FieldImporter mFieldImporter;
  @Mock RealObjectFieldTracker mRealObjectFieldTracker;

  Set<DependencyKey> emptyKeySet = new HashSet<>();

  List<TestResource> mTestResources = new LinkedList<>();

  ResourceLifecycleFieldManager mResourceLifecycleFieldManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mTestResources.add(new TestResource(new Object(), false));
    mTestResources.add(new TestResource(new Object(), true));
    mTestResources.add(new TestResource(new Object(), false));

    when(mRealObjectFieldTracker.keySet()).thenReturn(emptyKeySet);

    mResourceLifecycleFieldManager = new ResourceLifecycleFieldManager(
        mTestResources,
        mDependencyMap,
        mFieldImporter,
        mRealObjectFieldTracker);
  }

  @Test
  public void testSetup() {
    mResourceLifecycleFieldManager.setup(mMockspresso);

    InOrder inOrder = Mockito.inOrder(mDependencyMap, mFieldImporter, mRealObjectFieldTracker);
    for (TestResource resource : mTestResources) {
      inOrder.verify(mFieldImporter).importAnnotatedFields(resource.getObjectWithResources());
      inOrder.verify(mRealObjectFieldTracker).scanNullRealObjectFields(resource.getObjectWithResources());
    }
    inOrder.verify(mRealObjectFieldTracker).keySet();
    inOrder.verify(mDependencyMap).assertDoesNotContainAny(emptyKeySet);
    inOrder.verify(mRealObjectFieldTracker).applyValuesToFields();
    verifyNoMoreInteractions(mDependencyMap, mFieldImporter, mRealObjectFieldTracker);
  }

  @Test
  public void testTeardown() {
    mResourceLifecycleFieldManager.teardown();

    verify(mDependencyMap).clear();
    verify(mRealObjectFieldTracker).clear();
  }
}
