package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link DependencyMap}
 */
public class DependencyMapTest {

  interface TestInterface {}

  class TestClass implements TestInterface {}

  private DependencyMap mDependencyMap;

  @Before
  public void setup() {
    mDependencyMap = new DependencyMap();
  }

  @Test
  public void testSimplePutAndGet() {
    TestClass value = new TestClass();
    DependencyKey<TestClass> key = new DependencyKey<>(TypeToken.of(TestClass.class), null);

    mDependencyMap.put(key, value);
    TestClass output = mDependencyMap.get(key);

    assertThat(value).isEqualTo(output);
  }

  @Test
  public void testPutImplGetInterface() {
    TestClass value = new TestClass();
    DependencyKey<TestInterface> key = new DependencyKey<>(TypeToken.of(TestInterface.class), null);

    mDependencyMap.put(key, value);
    TestInterface output = mDependencyMap.get(key);

    assertThat(output)
        .isInstanceOf(TestClass.class)
        .isEqualTo(value);
  }
}
