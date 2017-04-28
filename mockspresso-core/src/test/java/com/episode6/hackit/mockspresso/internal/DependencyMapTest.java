package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
  public void testSimplePutAndClear() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestClass> key = DependencyKey.of(TestClass.class);

    dependencyMap.put(key, value, mPutValidator);
    dependencyMap.clear();
    TestClass output = dependencyMap.get(key, mGetValidator);

    assertThat(output).isNull();
  }

  @Test
  public void testParentPutAndChildClear() {
    DependencyMap parentMap = new DependencyMap();
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(parentMap);
    TestClass value = new TestClass();
    DependencyKey<TestClass> key = DependencyKey.of(TestClass.class);

    parentMap.put(key, value, mPutValidator);
    dependencyMap.clear();
    TestClass output = dependencyMap.get(key, mGetValidator);

    assertThat(value).isEqualTo(output);
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
}
