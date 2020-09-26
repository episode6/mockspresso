package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests {@link ResourcesLifecycleMethodManager}
 */
@RunWith(DefaultTestRunner.class)
public class ResourcesLifecycleMethodManagerTest {

  @Mock Mockspresso mMockspresso;

  final SampleTestRes testRes1 = new SampleTestRes();
  final SampleTestResSubclass testRes2 = new SampleTestResSubclass();
  final SampleTestRes testRes3 = new SampleTestRes();

  List<TestResource> mTestResources;
  ResourcesLifecycleMethodManager mResourcesLifecycleMethodManager;

  AutoCloseable mockitoClosable;

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);

    mTestResources = new ArrayList<>(3);
    mTestResources.add(new TestResource(testRes1, true));
    mTestResources.add(new TestResource(testRes2, true));
    mTestResources.add(new TestResource(testRes3, false));

    mResourcesLifecycleMethodManager = new ResourcesLifecycleMethodManager(mTestResources);
  }

  @Test
  public void testSetupOrder() {
    mResourcesLifecycleMethodManager.setup(mMockspresso);

    InOrder inOrder = Mockito.inOrder(getMocks());
    inOrder.verify(testRes1.notifier).setup();
    inOrder.verify(testRes2.notifier).setup();
    inOrder.verify(testRes2.subclassNotifier).setup(mMockspresso);
    verifyNoMoreInteractions(getMocks());
  }

  @Test
  public void testTeardownOrder() {
    mResourcesLifecycleMethodManager.teardown();

    InOrder inOrder = Mockito.inOrder(getMocks());
    inOrder.verify(testRes2.subclassNotifier).teardown();
    inOrder.verify(testRes2.notifier).teardown();
    inOrder.verify(testRes1.notifier).teardown();
    verifyNoMoreInteractions(getMocks());
  }

  private Object[] getMocks() {
    return new Object[]{testRes1.notifier, testRes2.subclassNotifier, testRes2.notifier, testRes3.notifier};
  }

  interface Notifier {
    void setup();

    void setup(Mockspresso instance);

    void teardown();
  }

  // since test resource methods are found via annotation, we cant mock or spy them
  private class SampleTestRes {
    Notifier notifier = mock(Notifier.class);

    @Before
    public void setup() {
      notifier.setup();
    }

    @After
    public void teardown() {
      notifier.teardown();
    }
  }

  private class SampleTestResSubclass extends SampleTestRes {
    Notifier subclassNotifier = mock(Notifier.class);

    @Before
    private void setupSubclass(Mockspresso mockspresso) {
      subclassNotifier.setup(mockspresso);
    }

    @After
    private void teardownSubclass() {
      subclassNotifier.teardown();
    }
  }
}
