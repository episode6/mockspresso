package com.episode6.hackit.mockspresso.plugin.javax;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link ProviderMaker}
 */
@RunWith(DefaultTestRunner.class)
public class ProviderMakerTest {

  private static final DependencyKey<TestClass> testClassKey = new DependencyKey<>(
      TypeToken.of(TestClass.class),
      null);
  private static final DependencyKey<Provider<TestClass>> testClassProviderKey = new DependencyKey<>(
      new TypeToken<Provider<TestClass>>() {},
      null);

  @Mock DependencyProvider mDependencyProvider;

  private final ProviderMaker mProviderMaker = ProviderMaker.getInstance();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCantCreateTestClass() {
    boolean canMake = mProviderMaker.canMakeObject(testClassKey);
    TestClass objMade = mProviderMaker.makeObject(mDependencyProvider, testClassKey);

    assertThat(canMake).isFalse();
    assertThat(objMade).isNull();
    verifyNoMoreInteractions(mDependencyProvider);
  }

  @Test
  public void testDoesCreateProvider() {
    TestClass expectedObj = new TestClass();
    when(mDependencyProvider.get(testClassKey)).thenReturn(expectedObj);

    boolean canMake = mProviderMaker.canMakeObject(testClassProviderKey);
    Provider<TestClass> objMade = mProviderMaker.makeObject(mDependencyProvider, testClassProviderKey);

    assertThat(canMake).isTrue();
    assertThat(objMade)
        .isNotNull()
        .isInstanceOf(Provider.class);
    assertThat(objMade.get())
        .isNotNull()
        .isEqualTo(expectedObj);
    verify(mDependencyProvider).get(testClassKey);
  }

  public static class TestClass {}
}