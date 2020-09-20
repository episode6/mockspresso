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

import javax.inject.Provider;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link DelayedMockspressoBuilder}
 */
@RunWith(DefaultTestRunner.class)
public class DelayedMockspressoBuilderTest {

  @Mock MockspressoBuilderImpl mBackingBuilder;
  @Mock Provider<MockspressoBuilderImpl> mBuilderProvider;

  @Mock MockspressoConfigContainer mConfig;

  @Mock MockspressoInternal mChildMockspresso;
  @Mock MockspressoConfigContainer mChildConfig;

  private DelayedMockspressoBuilder mDelayedBuilder;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mBuilderProvider.get()).thenReturn(mBackingBuilder);
    when(mBackingBuilder.deepCopy()).thenReturn(mBackingBuilder);
    when(mBackingBuilder.buildInternal()).thenReturn(mChildMockspresso);
    when(mChildMockspresso.getConfig()).thenReturn(mChildConfig);
    mDelayedBuilder = new DelayedMockspressoBuilder(mBuilderProvider);
  }

  @Test
  public void testCanBuildWithNothing() {
    Mockspresso mockspresso = mDelayedBuilder.build();

    assertThat(mockspresso)
        .isNotNull();
    verifyZeroInteractions(mBackingBuilder);
  }

  @Test
  public void testCanCreateWithParent() {
    Mockspresso mockspresso = mDelayedBuilder.build();
    mDelayedBuilder.setParent(mConfig);
    mockspresso.create(TestClass.class);

    InOrder inOrder = Mockito.inOrder(mBackingBuilder, mConfig, mChildMockspresso, mChildConfig);
    inOrder.verify(mBackingBuilder).deepCopy();
    inOrder.verify(mBackingBuilder).setParent(mConfig);
    inOrder.verify(mBackingBuilder).buildInternal();
    inOrder.verify(mChildMockspresso).getConfig();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(mChildMockspresso).create(TestClass.class);
    verifyNoMoreInteractions(mBackingBuilder, mConfig, mChildMockspresso, mChildConfig);
  }

  @Test
  public void testSetupAndTearDown() {
    mDelayedBuilder.setParent(mConfig);
    mDelayedBuilder.setParent(null);

    InOrder inOrder = Mockito.inOrder(mBackingBuilder, mConfig, mChildConfig, mChildMockspresso);
    inOrder.verify(mBackingBuilder).deepCopy();
    inOrder.verify(mBackingBuilder).setParent(mConfig);
    inOrder.verify(mBackingBuilder).buildInternal();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(mChildMockspresso).teardown();
    verifyNoMoreInteractions(mBackingBuilder, mConfig, mChildConfig);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateFailsWithoutParent() {
    Mockspresso mockspresso = mDelayedBuilder.build();
    mockspresso.create(TestClass.class);
  }

  @Test(expected = VerifyError.class)
  public void testFailOnBuildRule() {
    mDelayedBuilder.buildRule();
  }

  public static class TestClass {}
}
