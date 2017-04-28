package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.internal.AbstractDelayedMockspresso;
import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.internal.MockspressoConfigContainer;
import com.episode6.hackit.mockspresso.internal.MockspressoInternal;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
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
 * Tests {@link AbstractDelayedMockspresso}
 */
@RunWith(DefaultTestRunner.class)
public class AbstractDelayedMockspressoTest {

  @Mock MockspressoInternal mMockspressoInternal;
  @Mock MockspressoConfigContainer mConfig;
  @Mock Mockspresso.Builder mPublicBuilder;

  @Mock MockspressoBuilderImpl mChildBuilder;
  @Mock Provider<MockspressoBuilderImpl> mBuilderProvider;

  @Mock MockspressoInternal mChildMockspresso;
  @Mock MockspressoConfigContainer mChildConfig;

  private AbstractDelayedMockspresso mMockspresso;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mMockspressoInternal.getConfig()).thenReturn(mConfig);
    when(mBuilderProvider.get()).thenReturn(mChildBuilder);
    when(mChildBuilder.deepCopy()).thenReturn(mChildBuilder);
    when(mChildBuilder.buildInternal()).thenReturn(mChildMockspresso);
    when(mChildMockspresso.getConfig()).thenReturn(mChildConfig);
    when(mMockspressoInternal.buildUpon()).thenReturn(mPublicBuilder);

    mMockspresso = new AbstractDelayedMockspresso(mBuilderProvider) {};
  }

  @Test(expected = NullPointerException.class)
  public void testNPEOnEarlyCreateClass() {
    mMockspresso.create(TestClass.class);
  }

  @Test(expected = NullPointerException.class)
  public void testNPEOnEarlyCreateTypeToken() {
    mMockspresso.create(TypeToken.of(TestClass.class));
  }

  @Test(expected = NullPointerException.class)
  public void testNPEOnEarlyGetConfig() {
    mMockspresso.getConfig();
  }

  @Test
  public void testSimpleSetupAndTearDown() {
    mMockspresso.setDelegate(mMockspressoInternal);
    mMockspresso.create(String.class);
    mMockspresso.setDelegate(null);

    InOrder inOrder = Mockito.inOrder(mConfig, mMockspressoInternal);
    inOrder.verify(mConfig).setup(mMockspressoInternal);
    inOrder.verify(mMockspressoInternal).create(String.class);
    inOrder.verify(mConfig).teardown();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testChildSetupAndTeardown() {
    Mockspresso childMockspresso = mMockspresso.buildUpon().build();

    // set delegate before calling create, but after building the new instance
    mMockspresso.setDelegate(mMockspressoInternal);
    childMockspresso.create(TestClass.class);
    mMockspresso.setDelegate(null);


    InOrder inOrder = Mockito.inOrder(mBuilderProvider, mConfig, mChildBuilder, mChildConfig, mChildMockspresso);
    inOrder.verify(mBuilderProvider).get();
    inOrder.verify(mConfig).setup(mMockspressoInternal);
    inOrder.verify(mChildBuilder).deepCopy();
    inOrder.verify(mChildBuilder).setParent(mConfig);
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(mChildMockspresso).create(TestClass.class);
    inOrder.verify(mChildConfig).teardown();
    inOrder.verify(mConfig).teardown();
    inOrder.verifyNoMoreInteractions();
    verifyZeroInteractions(mPublicBuilder);
  }

  @Test(expected = NullPointerException.class)
  public void testEarlyBuildUponFailsWithoutDelegate() {
    Mockspresso childMockspresso = mMockspresso.buildUpon().build();
    childMockspresso.create(TestClass.class);
  }

  @Test
  public void testDelegateCreateClass() {
    mMockspresso.setDelegate(mMockspressoInternal);
    mMockspresso.create(TestClass.class);

    verify(mMockspressoInternal).create(TestClass.class);
  }

  @Test
  public void testDelegateCreateTypeToke() {
    TypeToken<TestClass> typeToken = TypeToken.of(TestClass.class);

    mMockspresso.setDelegate(mMockspressoInternal);
    mMockspresso.create(typeToken);

    verify(mMockspressoInternal).create(typeToken);
  }

  @Test
  public void testDelegateGetConfig() {
    mMockspresso.setDelegate(mMockspressoInternal);
    MockspressoConfigContainer returnedConfig = mMockspresso.getConfig();

    verify(mMockspressoInternal, times(2)).getConfig();
    assertThat(returnedConfig)
        .isNotNull()
        .isEqualTo(mConfig);
  }

  @Test
  public void testBuildUponDelegateConfig() {
    mMockspresso.setDelegate(mMockspressoInternal);
    Mockspresso.Builder returnedBuilder = mMockspresso.buildUpon();

    verify(mMockspressoInternal).buildUpon();
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mPublicBuilder);
  }

  private static class TestClass {}
}
