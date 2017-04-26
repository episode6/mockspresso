package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
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
 * Tests {@link ResourcesLifecycleManager}
 */
@RunWith(DefaultTestRunner.class)
public class ResourcesLifecycleManagerTest {

  @Mock Mockspresso mMockspresso;

  List<ResourcesLifecycleComponent> mLifecycleComponents;
  ResourcesLifecycleManager mResourcesLifecycleManager;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mLifecycleComponents = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      mLifecycleComponents.add(mock(ResourcesLifecycleComponent.class));
    }
    mResourcesLifecycleManager = new ResourcesLifecycleManager(mLifecycleComponents);
  }

  @Test
  public void testSetupOrder() {
    mResourcesLifecycleManager.setup(mMockspresso);

    InOrder inOrder = Mockito.inOrder(mLifecycleComponents.toArray());
    for (int i = 0; i < mLifecycleComponents.size(); i++) {
      inOrder.verify(mLifecycleComponents.get(i)).setup(mMockspresso);
    }
    verifyNoMoreInteractions(mLifecycleComponents.toArray());
  }

  @Test
  public void testTeardownOrder() {
    mResourcesLifecycleManager.teardown();

    InOrder inOrder = Mockito.inOrder(mLifecycleComponents.toArray());
    for (int i = mLifecycleComponents.size() - 1; i>=0; i--) {
      inOrder.verify(mLifecycleComponents.get(i)).teardown();
    }
    verifyNoMoreInteractions(mLifecycleComponents.toArray());
  }
}
