package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests {@link RealObjectMaker}
 */
@RunWith(DefaultTestRunner.class)
public class RealObjectMakerTest {

  static final DependencyKey<Runnable> runnableKey = new DependencyKey<>(TypeToken.of(Runnable.class), null);
  static final DependencyKey<Provider<Runnable>> runnableProviderKey = new DependencyKey<>(
      new TypeToken<Provider<Runnable>>() {},
      new NamedAnnotationLiteral("testprovider"));

  @Mock InjectionConfig.ConstructorSelector mConstructorSelector;
  @Mock DependencyProvider mDependencyProvider;

  @Mock Runnable mRunnableMock;
  @Mock Provider<Runnable> mRunnableProviderMock;

  private RealObjectMaker mRealObjectMaker;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    // testing with first constructor available
    when(mConstructorSelector.chooseConstructor(any(TypeToken.class)))
        .thenAnswer(new Answer<Constructor>() {
          @Override
          public Constructor answer(InvocationOnMock invocation) throws Throwable {
            TypeToken typeToken = invocation.getArgument(0);
            return typeToken.getRawType().getDeclaredConstructors()[0];
          }
        });
  }

  @Test
  public void testWithConstructorOnly() {
    prep();

    when(mDependencyProvider.get(runnableKey)).thenReturn(mRunnableMock);
    when(mDependencyProvider.get(runnableProviderKey)).thenReturn(mRunnableProviderMock);

    TestClassWithConstructorOnly testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithConstructorOnly.class));

    verify(mDependencyProvider).get(runnableKey);
    verify(mDependencyProvider).get(runnableProviderKey);

    assertThat(testObject).isNotNull();
    assertThat(mockingDetails(testObject).isMock()).isFalse();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithInjectOnly() {
    prep(Inject.class);

    when(mDependencyProvider.get(runnableKey)).thenReturn(mRunnableMock);
    when(mDependencyProvider.get(runnableProviderKey)).thenReturn(mRunnableProviderMock);

    TestClassWithInjectParams testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithInjectParams.class));

    verify(mDependencyProvider).get(runnableKey);
    verify(mDependencyProvider).get(runnableProviderKey);

    assertThat(testObject).isNotNull();
    assertThat(mockingDetails(testObject).isMock()).isFalse();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @Test
  public void testWithConstructorAndInjectMixed() {
    prep(Inject.class);

    when(mDependencyProvider.get(runnableKey)).thenReturn(mRunnableMock);
    when(mDependencyProvider.get(runnableProviderKey)).thenReturn(mRunnableProviderMock);

    TestConstructorAndInject testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestConstructorAndInject.class));

    verify(mDependencyProvider).get(runnableKey);
    verify(mDependencyProvider).get(runnableProviderKey);

    assertThat(testObject).isNotNull();
    assertThat(mockingDetails(testObject).isMock()).isFalse();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }


  @Test
  public void testWithWeirdInjectAnnotations() {
    prep(Inject.class, Singleton.class);

    when(mDependencyProvider.get(runnableKey)).thenReturn(mRunnableMock);
    when(mDependencyProvider.get(runnableProviderKey)).thenReturn(mRunnableProviderMock);

    TestClassWithWeirdInjectAnnotations testObject = mRealObjectMaker.createObject(
        mDependencyProvider,
        TypeToken.of(TestClassWithWeirdInjectAnnotations.class));

    verify(mDependencyProvider).get(runnableKey);
    verify(mDependencyProvider).get(runnableProviderKey);

    assertThat(testObject).isNotNull();
    assertThat(mockingDetails(testObject).isMock()).isFalse();
    assertThat(testObject.mRunnable).isEqualTo(mRunnableMock);
    assertThat(testObject.mRunnableProvider).isEqualTo(mRunnableProviderMock);
  }

  @SafeVarargs
  private final void prep(Class<? extends Annotation>... annotations) {
    List<Class<? extends Annotation>> annotationList = Arrays.asList(annotations);
    mRealObjectMaker = new RealObjectMaker(mConstructorSelector, annotationList);
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
}
