package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    assertThat(testObject.mRealRunnable).isEqualTo(expectedObj);
    verifyNoMoreInteractions(mRealObjectMaker, mDependencyMap, mDependencyProvider);
  }

  public static class TestClass1 {
    @RealObject String mPresetString = "teststring";
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnable;
    @TestQualifierAnnotation TestRunnable unboundRunnable;
  }

  public static class TestRunnable implements Runnable {
    @Override
    public void run() {

    }
  }
}
