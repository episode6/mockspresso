package com.episode6.hackit.mockspresso.mockito.integration;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.inject.Inject;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectByJavaxConfig;
import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectBySimpleConfig;
import static com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition;
import static com.episode6.hackit.mockspresso.mockito.MockspressoMockitoPluginsJavaSupport.mockByMockito;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 *
 */
@RunWith(JUnit4.class)
public class GenericConstructorTest {

  public static class TestObject {}
  public static class TestGeneric<V> {
    final V obj;
    public TestGeneric(V param) { obj = param; }
  }

  public static class TestInjectGeneric<V> {
    @Inject V obj;
    @Inject public TestInjectGeneric(){}
  }

  public static class TestMethodInjectGeneric<V> {
    V obj;
    @Inject public TestMethodInjectGeneric(){}
    @Inject public void inject(V obj) { this.obj = obj; }
  }

  public static class TestResources {
    @RealObject TestGeneric<TestObject> testGeneric;
  }

  public static class TestResourcesWithMock {
    @RealObject TestGeneric<TestObject> testGeneric;
    @Mock TestObject testMock;
  }

  public static class TestInjectResources {
    @RealObject TestInjectGeneric<TestObject> testGeneric;
  }

  public static class TestMethodInjectResources {
    @RealObject TestMethodInjectGeneric<TestObject> testGeneric;
  }

  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
      .plugin(injectBySimpleConfig())
      .plugin(mockByMockito())
      .buildRule();

  @Test public void testCreateViaTypeToken() {
    TestGeneric<TestObject> testGeneric = mockspresso.create(new TypeToken<TestGeneric<TestObject>>() {});

    assertThat(testGeneric).isNotNull();
    assertThat(testGeneric.obj).isNotNull().is(mockCondition());
  }

  @Test public void testCreateViaAnnotation() {
    TestResources resources = new TestResources();
    mockspresso.buildUpon().testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj).isNotNull().is(mockCondition());
  }

  @Test public void testCreateWithMockDep() {
    TestResourcesWithMock resources = new TestResourcesWithMock();
    mockspresso.buildUpon().testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj)
            .isNotNull()
            .is(mockCondition())
            .isEqualTo(resources.testMock);
  }

  @Test public void testInjectViaAnnotation() {
    TestInjectResources resources = new TestInjectResources();
    mockspresso.buildUpon().plugin(injectByJavaxConfig()).testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj).isNotNull().is(mockCondition());
  }

  @Test public void testMethodInjectViaAnnotation() {
    TestMethodInjectResources resources = new TestMethodInjectResources();
    mockspresso.buildUpon().plugin(injectByJavaxConfig()).testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj).isNotNull().is(mockCondition());
  }

  @Test public void testFieldInjectionOfExistingObject() {
    TestInjectGeneric<TestObject> testGeneric = new TestInjectGeneric<>();
    TestObject testMock = Mockito.mock(TestObject.class);

    mockspresso.buildUpon()
        .plugin(injectByJavaxConfig())
        .dependency(TestObject.class, testMock)
        .build()
        .inject(testGeneric, new TypeToken<TestInjectGeneric<TestObject>>() {});

    assertThat(testGeneric.obj)
        .isNotNull()
        .is(mockCondition())
        .isEqualTo(testMock);
  }

  @Test public void testMethodInjectionOfExistingObject() {
    TestMethodInjectGeneric<TestObject> testGeneric = new TestMethodInjectGeneric<>();
    TestObject testMock = Mockito.mock(TestObject.class);

    mockspresso.buildUpon()
        .plugin(injectByJavaxConfig())
        .dependency(TestObject.class, testMock)
        .build()
        .inject(testGeneric, new TypeToken<TestMethodInjectGeneric<TestObject>>() {});

    assertThat(testGeneric.obj)
        .isNotNull()
        .is(mockCondition())
        .isEqualTo(testMock);
  }
}
