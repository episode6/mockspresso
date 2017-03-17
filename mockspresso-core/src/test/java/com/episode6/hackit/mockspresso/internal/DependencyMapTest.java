package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests {@link DependencyMap}
 */
public class DependencyMapTest {

  interface TestInterface {}

  class TestClass implements TestInterface {}

  @Mock DependencyMap mMockDependencyMap;

  @Mock DependencyValidator mPutValidator;
  @Mock DependencyValidator mGetValidator;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSimplePutAndGet() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestClass> key = DependencyKey.of(TestClass.class);

    dependencyMap.put(key, value, mPutValidator);
    TestClass output = dependencyMap.get(key, mGetValidator);

    assertThat(value).isEqualTo(output);
    verify(mGetValidator).append(mPutValidator);
  }

  @Test
  public void testPutImplGetInterface() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestInterface> key = DependencyKey.of(TestInterface.class);

    dependencyMap.put(key, value, mPutValidator);
    TestInterface output = dependencyMap.get(key, mGetValidator);

    assertThat(output)
        .isInstanceOf(TestClass.class)
        .isEqualTo(value);
    verify(mGetValidator).append(mPutValidator);
  }

  @Test
  public void testParentCalledWhenDepMissing() {
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mMockDependencyMap);
    DependencyKey<TestInterface> key = DependencyKey.of(TestInterface.class);

    dependencyMap.containsKey(key);
    dependencyMap.get(key, mGetValidator);

    verify(mMockDependencyMap).containsKey(key);
    verify(mMockDependencyMap).get(key, mGetValidator);
  }

  @Test
  public void testParentNotCalledWhenDepExists() {
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mMockDependencyMap);
    DependencyKey<TestInterface> key = DependencyKey.of(TestInterface.class);
    TestClass value = new TestClass();
    dependencyMap.put(key, value, mPutValidator);

    boolean result = dependencyMap.containsKey(key);
    TestInterface resultObj = dependencyMap.get(key, mGetValidator);

    verifyNoMoreInteractions(mMockDependencyMap);
    assertThat(result).isTrue();
    assertThat(resultObj).isEqualTo(value);
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testCantOverwrite() {
    DependencyMap dependencyMap = new DependencyMap();
    DependencyKey<TestClass> key = DependencyKey.of(TestClass.class);
    TestClass value1 = new TestClass();
    TestClass value2 = new TestClass();

    dependencyMap.put(key, value1, mPutValidator);
    dependencyMap.put(key, value2, mPutValidator);
  }

  @Test
  public void testCanOverwriteParent() {
    DependencyMap parentMap = new DependencyMap();
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(parentMap);
    DependencyKey<TestClass> key = DependencyKey.of(TestClass.class);
    TestClass value1 = new TestClass();
    TestClass value2 = new TestClass();

    parentMap.put(key, value1, mPutValidator);
    dependencyMap.put(key, value2, mPutValidator);

    TestClass result = dependencyMap.get(key, mGetValidator);

    assertThat(result)
        .isEqualTo(value2)
        .isNotEqualTo(value1);
  }

  @Test
  public void testAssertDoesNotContainAnyPasses() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestClass> classKey = DependencyKey.of(TestClass.class);
    DependencyKey<TestInterface> interfaceKey = DependencyKey.of(TestInterface.class);

    dependencyMap.put(classKey, value, mPutValidator);
    dependencyMap.assertDoesNotContainAny(Collections.<DependencyKey>singleton(interfaceKey));
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testAssertDoesNotContainAnyFails() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestClass> classKey = DependencyKey.of(TestClass.class);
    DependencyKey<TestInterface> interfaceKey = DependencyKey.of(TestInterface.class);

    dependencyMap.put(classKey, value, mPutValidator);
    dependencyMap.assertDoesNotContainAny(Arrays.<DependencyKey>asList(interfaceKey, classKey));
  }
}
