package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests {@link RealObjectMaker}
 */
@SuppressWarnings("unchecked")
@RunWith(DefaultTestRunner.class)
public class RealObjectMakerTest {

  static final DependencyKey<Runnable> runnableKey = DependencyKey.of(Runnable.class);
  static final DependencyKey<Provider<Runnable>> runnableProviderKey = DependencyKey.of(
      new TypeToken<Provider<Runnable>>() {},
      new NamedAnnotationLiteral("testprovider"));

  @Mock InjectionConfig mInjectionConfig;
  @Mock DependencyProvider mDependencyProvider;

  @Mock Runnable mRunnableMock;
  @Mock Provider<Runnable> mRunnableProviderMock;

  private RealObjectMaker mRealObjectMaker;

  AutoCloseable mockitoClosable;

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);
    // testing with first constructor available
    when(mInjectionConfig.chooseConstructor(any(TypeToken.class)))
        .thenAnswer(new Answer<Constructor>() {
          @Override
          public Constructor answer(InvocationOnMock invocation) throws Throwable {
            TypeToken typeToken = invocation.getArgument(0);
            return typeToken.getRawType().getDeclaredConstructors()[0];
          }
        });

    when(mDependencyProvider.get(runnableKey)).thenReturn(mRunnableMock);
    when(mDependencyProvider.get(runnableProviderKey)).thenReturn(mRunnableProviderMock);
  }

  @Test
  public void testWithConstructorOnly() {
    prep();

    TestClassWithConstructorOnly testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithConstructorOnly.class));

    assertTestObjectNormal(testObject, TestClassWithConstructorOnly.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithInjectOnly() {
    prep(Inject.class);

    TestClassWithInjectParams testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithInjectParams.class));

    assertTestObjectNormal(testObject, TestClassWithInjectParams.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithInjectOnlyDirectInject() {
    prep(Inject.class);

    TestClassWithInjectParams testObject = new TestClassWithInjectParams();
    mRealObjectMaker.injectObject(
        mDependencyProvider,
        testObject,
        TypeToken.of(TestClassWithInjectParams.class));

    assertTestObjectNormal(testObject, TestClassWithInjectParams.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithConstructorAndInjectMixed() {
    prep(Inject.class);

    TestConstructorAndInject testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestConstructorAndInject.class));

    assertTestObjectNormal(testObject, TestConstructorAndInject.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithWeirdInjectAnnotations() {
    prep(Inject.class, Singleton.class);

    TestClassWithWeirdInjectAnnotations testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithWeirdInjectAnnotations.class));

    assertTestObjectNormal(testObject, TestClassWithWeirdInjectAnnotations.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithWeirdInjectAnnotationsDirectInject() {
    prep(Inject.class, Singleton.class);

    TestClassWithWeirdInjectAnnotations testObject = new TestClassWithWeirdInjectAnnotations();
    mRealObjectMaker.injectObject(
        mDependencyProvider,
        testObject,
        TypeToken.of(TestClassWithWeirdInjectAnnotations.class));

    assertTestObjectNormal(testObject, TestClassWithWeirdInjectAnnotations.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testSubclassInject() {
    prep(Inject.class);

    TestSubclass testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestSubclass.class));

    assertTestObjectNormal(testObject, TestSubclass.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testSubclassDirectInject() {
    prep(Inject.class);

    TestSubclass testObject = new TestSubclass();
    mRealObjectMaker.injectObject(
        mDependencyProvider,
        testObject,
        TypeToken.of(TestSubclass.class));

    assertTestObjectNormal(testObject, TestSubclass.class);
    verifyDependencyProviderCalls(runnableKey, runnableProviderKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testMethodInjection() {
    prep(Inject.class);

    TestMethodInjection testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestMethodInjection.class));

    assertTestObjectNormal(testObject, TestMethodInjection.class);
    verifyDependencyProviderCalls(runnableKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
  }

  @Test
  public void testMethodDirectInjection() {
    prep(Inject.class);

    TestMethodInjection testObject = new TestMethodInjection();
    mRealObjectMaker.injectObject(
        mDependencyProvider,
        testObject,
        TypeToken.of(TestMethodInjection.class));

    assertTestObjectNormal(testObject, TestMethodInjection.class);
    verifyDependencyProviderCalls(runnableKey);
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
  }

  @Test
  public void testSubclassMethodInject() {
    prep(Inject.class);

    TestMethodInjectionSubclass testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestMethodInjectionSubclass.class));

    assertTestObjectNormal(testObject, TestMethodInjectionSubclass.class);
    // here we want to verify that the super class's injectable method is called first
    InOrder inOrder = inOrder(mDependencyProvider);
    inOrder.verify(mDependencyProvider).get(runnableKey);
    inOrder.verify(mDependencyProvider).get(runnableProviderKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testSubclassMethodDirectInject() {
    prep(Inject.class);

    TestMethodInjectionSubclass testObject = new TestMethodInjectionSubclass();
    mRealObjectMaker.injectObject(mDependencyProvider, testObject, TypeToken.of(TestMethodInjectionSubclass.class));

    assertTestObjectNormal(testObject, TestMethodInjectionSubclass.class);
    // here we want to verify that the super class's injectable method is called first
    InOrder inOrder = inOrder(mDependencyProvider);
    inOrder.verify(mDependencyProvider).get(runnableKey);
    inOrder.verify(mDependencyProvider).get(runnableProviderKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @SafeVarargs
  private final void prep(Class<? extends Annotation>... annotations) {
    List<Class<? extends Annotation>> annotationList = Arrays.asList(annotations);
    when(mInjectionConfig.provideInjectableMethodAnnotations()).thenReturn(annotationList);
    when(mInjectionConfig.provideInjectableFieldAnnotations()).thenReturn(annotationList);
    mRealObjectMaker = new RealObjectMaker(mInjectionConfig);
  }

  private <T> void assertTestObjectNormal(T testObject, Class<T> expectedClass) {
    assertThat(testObject)
        .isNotNull()
        .isNot(mockitoMock())
        .is(rawClass(expectedClass));
  }

  private void verifyDependencyProviderCalls(DependencyKey... keys) {
    for (DependencyKey key : keys) {
      verify(mDependencyProvider).get(key);
    }
  }

  public static class TestClassWithConstructorOnly {
    final Runnable mRunnable;
    final Provider<Runnable> mRunnableProvider;

    public TestClassWithConstructorOnly(
        Runnable runnable,
        @Named("testprovider") Provider<Runnable> runnableProvider) {
      mRunnable = runnable;
      mRunnableProvider = runnableProvider;
    }
  }

  public static class TestClassWithInjectParams {
    @Inject Runnable mRunnable;
    @Inject @Named("testprovider") Provider<Runnable> mRunnableProvider;
  }

  public static class TestConstructorAndInject {
    final Runnable mRunnable;
    @Inject @Named("testprovider") Provider<Runnable> mRunnableProvider;

    public TestConstructorAndInject(Runnable runnable) {
      mRunnable = runnable;
    }
  }

  public static class TestClassWithWeirdInjectAnnotations {
    @Inject Runnable mRunnable;
    @Singleton @Named("testprovider") Provider<Runnable> mRunnableProvider;
  }

  public static class TestSubclass extends TestClassWithInjectParams {}

  public static class TestMethodInjection {
    Runnable mRunnable;

    @Inject
    public void injectMe(Runnable runnable) {
      mRunnable = runnable;
    }
  }

  public static class TestMethodInjectionSubclass extends TestMethodInjection {
    Provider<Runnable> mRunnableProvider;

    @Inject
    private void injectMeToo(@Named("testprovider") Provider<Runnable> runnableProvider) {
      mRunnableProvider = runnableProvider;
    }
  }
}
