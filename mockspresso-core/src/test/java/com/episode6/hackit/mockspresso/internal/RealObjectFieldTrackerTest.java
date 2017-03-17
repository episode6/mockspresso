package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.exception.RealObjectMappingMismatchException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests {@link RealObjectFieldTracker}
 */
@RunWith(DefaultTestRunner.class)
public class RealObjectFieldTrackerTest {
  private static final DependencyKey<TestRunnable> testRunnableKey = DependencyKey.of(TestRunnable.class);
  private static final DependencyKey<Runnable> runnableKey = DependencyKey.of(Runnable.class);

  @Mock RealObjectMapping mRealObjectMapping;

  private RealObjectFieldTracker mRealObjectFieldTracker;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    mRealObjectFieldTracker = new RealObjectFieldTracker(mRealObjectMapping);
  }

  @Test
  public void testBasicScanning() {
    TestClass1 testObject = new TestClass1();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);

    verify(mRealObjectMapping).put(runnableKey, testRunnableKey.typeToken, true);
    verify(mRealObjectMapping).put(testRunnableKey, testRunnableKey.typeToken, true);
    verifyNoMoreInteractions(mRealObjectMapping);

    assertThat(mRealObjectFieldTracker.keySet()).containsOnly(runnableKey, testRunnableKey);
  }

  @Test
  public void testScanThenSet() {
    TestClass1 testObject = new TestClass1();
    TestRunnable valueForRunnableKey = new TestRunnable();
    TestRunnable valueForTestRunnableKey = new TestRunnable();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);
    mRealObjectFieldTracker.applyValueToFields(runnableKey, valueForRunnableKey);
    mRealObjectFieldTracker.applyValueToFields(testRunnableKey, valueForTestRunnableKey);

    assertThat(testObject.mRealRunnableWithImpl)
        .isNotNull()
        .isEqualTo(valueForRunnableKey);
    assertThat(testObject.mRealTestRunnable)
        .isNotNull()
        .isEqualTo(valueForTestRunnableKey);
  }

  @Test
  public void testSameObjectMappedTwice() {
    TestClass1 testObject1 = new TestClass1();
    TestClass2 testObject2 = new TestClass2();
    TestRunnable valueForRunnableKey = new TestRunnable();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
    mRealObjectFieldTracker.applyValueToFields(runnableKey, valueForRunnableKey);

    assertThat(testObject1.mRealRunnableWithImpl)
        .isNotNull()
        .isEqualTo(testObject2.mRealRunnableWithImpl)
        .isEqualTo(valueForRunnableKey);
  }

  @Test(expected = RealObjectMappingMismatchException.class)
  public void testMisMatch() {
    TestClass1 testObject1 = new TestClass1();
    TestClassMisMatch testObject2 = new TestClassMisMatch();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
  }

  public static class TestClass1 {
    @RealObject String mPresetString = "teststring"; // ignored because it's non-null
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnableWithImpl;
    @RealObject TestRunnable mRealTestRunnable;
    @TestQualifierAnnotation TestRunnable unboundRunnable; // ignored because it has no @RealObject annotation
  }

  public static class TestClass2 {
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnableWithImpl;
  }

  public static class TestClassMisMatch {
    @RealObject Runnable mRunnable;
  }

  public static class TestRunnable implements Runnable {
    @Override
    public void run() {

    }
  }
}
