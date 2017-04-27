package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.exception.RealObjectMappingMismatchException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link RealObjectFieldTracker}
 */
@RunWith(DefaultTestRunner.class)
public class RealObjectFieldTrackerTest {
  private static final DependencyKey<TestRunnable> testRunnableKey = DependencyKey.of(TestRunnable.class);
  private static final DependencyKey<Runnable> runnableKey = DependencyKey.of(Runnable.class);

  @Mock RealObjectMapping mRealObjectMapping;
  @Mock RealObjectMaker mRealObjectMaker;
  @Mock DependencyProviderFactory mDependencyProviderFactory;
  @Mock DependencyProvider mBlankDependencyProvider;

  private RealObjectFieldTracker mRealObjectFieldTracker;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(mDependencyProviderFactory.getBlankDependencyProvider()).thenReturn(mBlankDependencyProvider);

    mRealObjectFieldTracker = new RealObjectFieldTracker(mRealObjectMapping, mRealObjectMaker, mDependencyProviderFactory);
  }

  @Test
  public void testBasicScanning() {
    TestClass1 testObject = new TestClass1();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);

    verify(mRealObjectMapping).put(runnableKey, testRunnableKey.typeToken, true);
    verify(mRealObjectMapping).put(testRunnableKey, testRunnableKey.typeToken, true);
    verifyNoMoreInteractions(mRealObjectMapping);

    assertThat(mRealObjectFieldTracker.mappedKeys()).containsOnly(runnableKey, testRunnableKey);
  }

  @Test
  public void testBasicScanningWithUnmappedRealObjects() {
    TestClass1WithUnmapped testObject = new TestClass1WithUnmapped();

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);
    mRealObjectFieldTracker.applyValuesToFields();

    verify(mRealObjectMapping).put(runnableKey, testRunnableKey.typeToken, true);
    verify(mDependencyProviderFactory).getBlankDependencyProvider();
    verify(mBlankDependencyProvider).get(runnableKey);
    verify(mDependencyProviderFactory, times(2)).getDependencyProviderFor(testRunnableKey);
    verify(mDependencyProviderFactory).getDependencyProviderFor(runnableKey);
    verify(mRealObjectMaker, times(3)).createObject(null, testRunnableKey.typeToken);

    verifyNoMoreInteractions(mRealObjectMapping, mDependencyProviderFactory, mRealObjectMaker, mBlankDependencyProvider);

    assertThat(mRealObjectFieldTracker.mappedKeys()).containsOnly(runnableKey);
  }

  @Test
  public void testScanThenSet() {
    TestClass1 testObject = new TestClass1();
    TestRunnable valueForRunnableKey = new TestRunnable();
    TestRunnable valueForTestRunnableKey = new TestRunnable();
    when(mBlankDependencyProvider.get(runnableKey)).thenReturn(valueForRunnableKey);
    when(mBlankDependencyProvider.get(testRunnableKey)).thenReturn(valueForTestRunnableKey);

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);
    mRealObjectFieldTracker.applyValuesToFields();

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
    when(mBlankDependencyProvider.get(runnableKey)).thenReturn(valueForRunnableKey);

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject1);
    mRealObjectFieldTracker.scanNullRealObjectFields(testObject2);
    mRealObjectFieldTracker.applyValuesToFields();

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

  @Test
  public void testClear() {
    TestClass1WithUnmapped testObject = new TestClass1WithUnmapped();
    TestRunnable valueForRunnableKey = new TestRunnable();
    TestRunnable valueForTestRunnableKey = new TestRunnable();
    when(mBlankDependencyProvider.get(runnableKey)).thenReturn(valueForRunnableKey);
    when(mRealObjectMaker.createObject(null, testRunnableKey.typeToken))
        .thenReturn(valueForTestRunnableKey);

    mRealObjectFieldTracker.scanNullRealObjectFields(testObject);
    mRealObjectFieldTracker.applyValuesToFields();

    assertThat(testObject.mRealRunnableWithImpl)
        .isNotNull()
        .isEqualTo(valueForRunnableKey);
    assertThat(testObject.mRealTestRunnable)
        .isNotNull()
        .isEqualTo(valueForTestRunnableKey);
    assertThat(testObject.mRealRunnableWithImpl2)
        .isNotNull()
        .isEqualTo(valueForTestRunnableKey);
    assertThat(testObject.mRealTestRunnable2)
        .isNotNull()
        .isEqualTo(valueForTestRunnableKey);

    mRealObjectFieldTracker.clear();

    verify(mRealObjectMapping).clear();
    assertThat(mRealObjectFieldTracker.mappedKeys()).isEmpty();
    assertThat(testObject.mRealRunnableWithImpl).isNull();
    assertThat(testObject.mRealTestRunnable).isNull();
    assertThat(testObject.mRealRunnableWithImpl2).isNull();
    assertThat(testObject.mRealTestRunnable2).isNull();
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

  public static class TestClass1WithUnmapped {
    @Unmapped @RealObject String mPresetString = "teststring"; // ignored because it's non-null
    @RealObject(implementation = TestRunnable.class) Runnable mRealRunnableWithImpl;
    @Unmapped @RealObject TestRunnable mRealTestRunnable;
    @Unmapped @RealObject(implementation = TestRunnable.class) Runnable mRealRunnableWithImpl2;
    @Unmapped @RealObject TestRunnable mRealTestRunnable2;
  }
}
