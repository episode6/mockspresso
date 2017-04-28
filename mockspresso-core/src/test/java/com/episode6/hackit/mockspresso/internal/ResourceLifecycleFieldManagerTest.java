package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
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
  @Mock RealObjectMapping mRealObjectMapping;
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

    when(mDependencyMap.keySet()).thenReturn(emptyKeySet);
    when(mRealObjectMapping.keySet()).thenReturn(emptyKeySet);


    mResourceLifecycleFieldManager = new ResourceLifecycleFieldManager(
        mTestResources,
        mDependencyMap,
        mRealObjectMapping,
        mFieldImporter,
        mRealObjectFieldTracker);
  }

  @Test
  public void testSetup() {
    mResourceLifecycleFieldManager.setup(mMockspresso);

    InOrder inOrder = Mockito.inOrder(mDependencyMap, mRealObjectMapping, mFieldImporter, mRealObjectFieldTracker);
    for (TestResource resource : mTestResources) {
      inOrder.verify(mFieldImporter).importAnnotatedFields(resource.getObjectWithResources());
      inOrder.verify(mRealObjectFieldTracker).scanNullRealObjectFields(resource.getObjectWithResources());
    }
    inOrder.verify(mDependencyMap).keySet();
    inOrder.verify(mRealObjectMapping).keySet();
    inOrder.verify(mRealObjectFieldTracker).applyValuesToFields();
    verifyNoMoreInteractions(mDependencyMap, mRealObjectMapping, mFieldImporter, mRealObjectFieldTracker);
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testRepeatedDependencyBetweenDepAndRealObjMapping() {
    Set<DependencyKey> dependencyMapKeys = new HashSet<>();
    dependencyMapKeys.add(DependencyKey.of(String.class));
    Set<DependencyKey> realObjectMapKeys = new HashSet<>();
    realObjectMapKeys.add(DependencyKey.of(String.class));
    when(mDependencyMap.keySet()).thenReturn(dependencyMapKeys);
    when(mRealObjectMapping.keySet()).thenReturn(realObjectMapKeys);

    mResourceLifecycleFieldManager.setup(mMockspresso);
  }

  @Test
  public void testTeardown() {
    mResourceLifecycleFieldManager.teardown();

    verify(mDependencyMap).clear();
    verify(mRealObjectFieldTracker).clear();
    verify(mRealObjectMapping).clear();
  }
}
