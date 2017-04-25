package com.episode6.hackit.mockspresso.internal.delayed;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.internal.MockspressoConfigContainer;
import com.episode6.hackit.mockspresso.internal.MockspressoInternal;
import com.episode6.hackit.mockspresso.mockito.MockitoMockerConfig;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectionConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link AbstractDelayedMockspresso}
 */
@RunWith(DefaultTestRunner.class)
public class AbstractDelayedMockspressoTest {

  @Mock MockspressoInternal mMockspressoInternal;
  @Mock MockspressoConfigContainer mMockspressoConfigContainer;
  @Mock Mockspresso.Builder mMockBuilder;

  private final AbstractDelayedMockspresso mMockspresso = new AbstractDelayedMockspresso() {};

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mMockspressoInternal.getConfig()).thenReturn(mMockspressoConfigContainer);
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
  public void testEarlyBuildUponWorksWithAfterTheFactDelegate() {
    when(mMockspressoConfigContainer.getInjectionConfig()).thenReturn(SimpleInjectionConfig.getInstance());
    when(mMockspressoConfigContainer.getMockerConfig()).thenReturn(MockitoMockerConfig.getInstance());

    Mockspresso childMockspresso = mMockspresso.buildUpon().build();
    mMockspresso.setDelegate(mMockspressoInternal);
    // once we call setDelegate, a real mockspresso instance is created
    TestClass returnedObject = childMockspresso.create(TestClass.class);

    verify(mMockspressoInternal).getConfig();
    assertThat(returnedObject)
        .isNotNull()
        .isNot(mockitoMock())
        .is(rawClass(TestClass.class));
  }

  @Test(expected = NullPointerException.class)
  public void testEarlyBuildUponFailsWithoutDelegate() {
    Mockspresso childMockspresso = mMockspresso.buildUpon().build();
    childMockspresso.create(TestClass.class);
  }

  @Test
  public void testDelegateCreateClass() {
    TestClass expectedObj = new TestClass();
    when(mMockspressoInternal.create(TestClass.class)).thenReturn(expectedObj);

    mMockspresso.setDelegate(mMockspressoInternal);
    TestClass returnedObj = mMockspresso.create(TestClass.class);

    verify(mMockspressoInternal).create(TestClass.class);
    assertThat(returnedObj)
        .isNotNull()
        .isEqualTo(expectedObj);
  }

  @Test
  public void testDelegateCreateTypeToke() {
    TestClass expectedObj = new TestClass();
    TypeToken<TestClass> typeToken = TypeToken.of(TestClass.class);
    when(mMockspressoInternal.create(typeToken)).thenReturn(expectedObj);

    mMockspresso.setDelegate(mMockspressoInternal);
    TestClass returnedObj = mMockspresso.create(typeToken);

    verify(mMockspressoInternal).create(typeToken);
    assertThat(returnedObj)
        .isNotNull()
        .isEqualTo(expectedObj);
  }

  @Test
  public void testDelegateGetConfig() {
    when(mMockspressoInternal.getConfig()).thenReturn(mMockspressoConfigContainer);

    mMockspresso.setDelegate(mMockspressoInternal);
    MockspressoConfigContainer returnedConfig = mMockspresso.getConfig();

    verify(mMockspressoInternal, times(2)).getConfig();
    assertThat(returnedConfig)
        .isNotNull()
        .isEqualTo(mMockspressoConfigContainer);
  }

  @Test
  public void testBuildUponDelegateConfig() {
    when(mMockspressoInternal.buildUpon()).thenReturn(mMockBuilder);

    mMockspresso.setDelegate(mMockspressoInternal);
    Mockspresso.Builder returnedBuilder = mMockspresso.buildUpon();

    verify(mMockspressoInternal).buildUpon();
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mMockBuilder);
  }

  private static class TestClass {}
}
