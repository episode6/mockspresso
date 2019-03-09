package com.episode6.hackit.mockspresso.mockito.integration;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.quick.BuildQuickMockspresso;
import com.episode6.hackit.mockspresso.quick.QuickMockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import javax.inject.Inject;

import static com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition;
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

  @Rule public final QuickMockspresso.Rule mockspresso = BuildQuickMockspresso.with()
      .injector().simple()
      .mocker().mockito()
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
    mockspresso.buildUpon().injector().javax().testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj).isNotNull().is(mockCondition());
  }

  @Test public void testMethodInjectViaAnnotation() {
    TestMethodInjectResources resources = new TestMethodInjectResources();
    mockspresso.buildUpon().injector().javax().testResources(resources).build();

    assertThat(resources.testGeneric).isNotNull();
    assertThat(resources.testGeneric.obj).isNotNull().is(mockCondition());
  }
}
