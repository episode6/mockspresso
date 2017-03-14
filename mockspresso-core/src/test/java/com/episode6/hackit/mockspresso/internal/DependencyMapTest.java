package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
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

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSimplePutAndGet() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestClass> key = new DependencyKey<>(TypeToken.of(TestClass.class), null);

    dependencyMap.put(key, value);
    TestClass output = dependencyMap.get(key);

    assertThat(value).isEqualTo(output);
  }

  @Test
  public void testPutImplGetInterface() {
    DependencyMap dependencyMap = new DependencyMap();
    TestClass value = new TestClass();
    DependencyKey<TestInterface> key = new DependencyKey<>(TypeToken.of(TestInterface.class), null);

    dependencyMap.put(key, value);
    TestInterface output = dependencyMap.get(key);

    assertThat(output)
        .isInstanceOf(TestClass.class)
        .isEqualTo(value);
  }

  @Test
  public void testParentCalledWhenDepMissing() {
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mMockDependencyMap);
    DependencyKey<TestInterface> key = new DependencyKey<>(TypeToken.of(TestInterface.class), null);

    dependencyMap.containsKey(key);
    dependencyMap.get(key);

    verify(mMockDependencyMap).containsKey(key);
    verify(mMockDependencyMap).get(key);
  }

  @Test
  public void testParentNotCalledWhenDepExists() {
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mMockDependencyMap);
    DependencyKey<TestInterface> key = new DependencyKey<>(TypeToken.of(TestInterface.class), null);
    TestClass value = new TestClass();
    dependencyMap.put(key, value);

    boolean result = dependencyMap.containsKey(key);
    TestInterface resultObj = dependencyMap.get(key);

    verifyNoMoreInteractions(mMockDependencyMap);
    assertThat(result).isTrue();
    assertThat(resultObj).isEqualTo(value);
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testCantOverwrite() {
    DependencyMap dependencyMap = new DependencyMap();
    DependencyKey<TestClass> key = new DependencyKey<>(TypeToken.of(TestClass.class), null);
    TestClass value1 = new TestClass();
    TestClass value2 = new TestClass();

    dependencyMap.put(key, value1);
    dependencyMap.put(key, value2);
  }

  @Test
  public void testCanOverwriteParent() {
    DependencyMap parentMap = new DependencyMap();
    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(parentMap);
    DependencyKey<TestClass> key = new DependencyKey<>(TypeToken.of(TestClass.class), null);
    TestClass value1 = new TestClass();
    TestClass value2 = new TestClass();

    parentMap.put(key, value1);
    dependencyMap.put(key, value2);

    TestClass result = dependencyMap.get(key);

    assertThat(result)
        .isEqualTo(value2)
        .isNotEqualTo(value1);
  }
}
