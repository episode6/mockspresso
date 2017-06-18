package com.episode6.hackit.mockspresso.dagger2;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import dagger.Lazy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link Dagger2LazyMaker}
 */
public class Dagger2LazyMakerTest {
  private static final DependencyKey<TestClass> testClassKey = DependencyKey.of(TestClass.class);
  private static final DependencyKey<Lazy<TestClass>> testClassProviderKey =
      DependencyKey.of(new TypeToken<Lazy<TestClass>>() {});

  @Mock DependencyProvider mDependencyProvider;

  private final Dagger2LazyMaker mLazyMaker = new Dagger2LazyMaker();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
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

  public static class TestClass {}
}
