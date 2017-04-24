package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests {@link MockspressoImpl}
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoImplTest {

  @Mock MockspressoConfigContainer mConfig;
  @Mock DependencyProviderFactory mDependencyProviderFactory;
  @Mock RealObjectMaker mRealObjectMaker;
  @Mock DependencyProvider mDependencyProvider;

  MockspressoImpl mMockspresso;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mDependencyProviderFactory.getDependencyProviderFor(any(DependencyKey.class))).thenReturn(mDependencyProvider);

    mMockspresso = new MockspressoImpl(mConfig, mDependencyProviderFactory, mRealObjectMaker);
  }

  @Test
  public void testBasicUsage() {
    mMockspresso.create(String.class);
    mMockspresso.create(TypeToken.of(Integer.class));
    mMockspresso.buildUpon();
    MockspressoConfigContainer configContainer = mMockspresso.getConfig();

    InOrder inOrder = Mockito.inOrder(mDependencyProviderFactory, mRealObjectMaker, mConfig);
    inOrder.verify(mDependencyProviderFactory).getDependencyProviderFor(DependencyKey.of(String.class));
    inOrder.verify(mRealObjectMaker).createObject(mDependencyProvider, TypeToken.of(String.class));
    inOrder.verify(mDependencyProviderFactory).getDependencyProviderFor(DependencyKey.of(Integer.class));
    inOrder.verify(mRealObjectMaker).createObject(mDependencyProvider, TypeToken.of(Integer.class));
    inOrder.verify(mConfig).newBuilder();
    inOrder.verifyNoMoreInteractions();

    assertThat(configContainer).isEqualTo(mConfig);
  }
}
