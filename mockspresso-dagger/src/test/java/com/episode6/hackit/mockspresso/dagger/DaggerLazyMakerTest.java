package com.episode6.hackit.mockspresso.dagger;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import dagger.Lazy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link DaggerLazyMaker}
 */
public class DaggerLazyMakerTest {
  private static final DependencyKey<TestClass> testClassKey = DependencyKey.of(TestClass.class);
  private static final DependencyKey<Lazy<TestClass>> testClassProviderKey =
      DependencyKey.of(new TypeToken<Lazy<TestClass>>() {});

  @Mock DependencyProvider mDependencyProvider;

  private final DaggerLazyMaker mLazyMaker = new DaggerLazyMaker();

  AutoCloseable mockitoClosable;

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCantCreateTestClass() {
    boolean canMake = mLazyMaker.canMakeObject(testClassKey);
    TestClass objMade = mLazyMaker.makeObject(mDependencyProvider, testClassKey);

    assertThat(canMake).isFalse();
    assertThat(objMade).isNull();
    verifyNoMoreInteractions(mDependencyProvider);
  }

  @Test
  public void testDoesCreateProvider() {
    TestClass expectedObj = new TestClass();
    when(mDependencyProvider.get(testClassKey)).thenReturn(expectedObj);

    boolean canMake = mLazyMaker.canMakeObject(testClassProviderKey);
    Lazy<TestClass> objMade = mLazyMaker.makeObject(mDependencyProvider, testClassProviderKey);

    assertThat(canMake).isTrue();
    assertThat(objMade)
        .isNotNull()
        .isInstanceOf(Lazy.class);
    assertThat(objMade.get())
        .isNotNull()
        .isEqualTo(expectedObj);
    verify(mDependencyProvider).get(testClassKey);
  }

  @Test
  public void testCantCreateUnParameterizedProvider() {
    boolean canMake = mLazyMaker.canMakeObject(DependencyKey.of(Lazy.class));
    Lazy lazy = mLazyMaker.makeObject(mDependencyProvider, DependencyKey.of(Lazy.class));

    assertThat(canMake).isFalse();
    assertThat(lazy).isNull();
    verifyNoMoreInteractions(mDependencyProvider);
  }

  public static class TestClass {}
}
