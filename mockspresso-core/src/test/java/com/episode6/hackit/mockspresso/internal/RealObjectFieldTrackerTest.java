package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.exception.RealObjectMappingMismatchException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Provider;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests {@link RealObjectFieldTracker}
 */
@RunWith(DefaultTestRunner.class)
public class RealObjectFieldTrackerTest {
  private static final DependencyKey<TestRunnable> testRunnableKey = new DependencyKey<>(TypeToken.of(TestRunnable.class), null);
  private static final DependencyKey<Runnable> runnableKey = new DependencyKey<>(TypeToken.of(Runnable.class), null);
  private static final DependencyKey<TestProvider> testProviderKey = new DependencyKey<>(TypeToken.of(TestProvider.class), null);

  @Mock RealObjectMaker mRealObjectMaker;
  @Mock DependencyMap mDependencyMap;
  @Mock DependencyProvider mDependencyProvider;

  private RealObjectFieldTracker mRealObjectFieldTracker;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mRealObjectFieldTracker = new RealObjectFieldTracker(
        mRealObjectMaker,
        mDependencyMap,
        mDependencyProvider);
  }

  @Test
  public void testCreateAndApply() {
    TestClass1 testObject = new TestClass1();
    TestRunnable expectedObj = new TestRunnable();
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken)))
        .thenReturn(expectedObj);

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);
    mRealObjectFieldTracker.createAndAssignTrackedRealObjects();

    verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken));
    verify(mDependencyMap).put(runnableKey, expectedObj);
    verifyNoMoreInteractions(mRealObjectMaker, mDependencyMap, mDependencyProvider);

    assertThat(testObject.mRealRunnable).isEqualTo(expectedObj);
  }

  @Test
  public void testSameObjectMappedTwice() {
    TestClass1 testObject1 = new TestClass1();
    TestClass2 testObject2 = new TestClass2();
    TestRunnable expectedObj = new TestRunnable();
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken)))
        .thenReturn(expectedObj);

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
    mRealObjectFieldTracker.createAndAssignTrackedRealObjects();

    verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken));
    verify(mDependencyMap).put(runnableKey, expectedObj);
    verifyNoMoreInteractions(mRealObjectMaker, mDependencyMap, mDependencyProvider);

    assertThat(testObject1.mRealRunnable)
        .isEqualTo(testObject2.mRealRunnable)
        .isEqualTo(expectedObj);
  }

  @Test(expected = RealObjectMappingMismatchException.class)
  public void testMisMatch() {
    TestClass1 testObject1 = new TestClass1();
    TestClassMisMatch testObject2 = new TestClassMisMatch();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
    mRealObjectFieldTracker.createAndAssignTrackedRealObjects();
  }

  @Test
  public void testDependencyProvider() {
    TestClassDependencies testObject1 = new TestClassDependencies();
    TestClass2 testObject2 = new TestClass2();
    TestRunnable expectedRunnable = new TestRunnable();
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken)))
        .thenReturn(expectedRunnable);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        // when we put the Runnable in the dependency map, setup the dependencyProvider to return it
        // this is a functionality that we depend on.
        Runnable value = invocation.getArgument(1);
        when(mDependencyProvider.get(runnableKey)).thenReturn(value);
        return null;
      }
    }).when(mDependencyMap).put(eq(runnableKey), any(Runnable.class));
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(testProviderKey.typeToken)))
        .thenAnswer(new Answer<TestProvider>() {
          @Override
          public TestProvider answer(InvocationOnMock invocation) throws Throwable {
            // this is how we'd expect the real object maker to act. TestProvider has a dependency on
            // Runnable, so the dependency provider should be queried, and the result used to create the object
            DependencyProvider dependencyProvider = invocation.getArgument(0);
            Runnable runnable = dependencyProvider.get(runnableKey);
            return new TestProvider(runnable);
          }
        });

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
    mRealObjectFieldTracker.createAndAssignTrackedRealObjects();

    verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(testProviderKey.typeToken));
    verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(testRunnableKey.typeToken));
    verify(mDependencyMap).put(runnableKey, expectedRunnable);
    verify(mDependencyProvider).get(runnableKey);
    verify(mDependencyMap).put(eq(testProviderKey), any(TestProvider.class));
    verifyNoMoreInteractions(mRealObjectMaker, mDependencyMap, mDependencyProvider);

    assertThat(testObject1.mTestProvider.get())
        .isEqualTo(testObject2.mRealRunnable)
        .isEqualTo(expectedRunnable);
  }

  public static class TestClass1 {
    @RealObject String mPresetString = "teststring"; // ignored because it's non-null
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnable;
    @TestQualifierAnnotation TestRunnable unboundRunnable; // ignored because it has no @RealObject annotation
  }

  public static class TestClass2 {
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnable;
  }

  public static class TestClassDependencies {
    @RealObject TestProvider mTestProvider;
  }

  public static class TestClassMisMatch {
    @RealObject Runnable mRunnable;
  }

  public static class TestRunnable implements Runnable {
    @Override
    public void run() {

    }
  }

  public static class TestProvider implements Provider<Runnable> {

    private final Runnable mRunnable;

    public TestProvider(Runnable runnable) {
      mRunnable = runnable;
    }

    @Override
    public Runnable get() {
      return mRunnable;
    }
  }
}
