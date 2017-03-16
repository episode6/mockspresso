package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests {@link RealObjectMapping}
 */
@RunWith(DefaultTestRunner.class)
public class RealObjectMappingTest {

  interface TestInterface {}

  class TestClass implements TestInterface {}
  class TestClass2 implements TestInterface {}

  private static final TypeToken<TestInterface> testInterfaceTypeToken = TypeToken.of(TestInterface.class);
  private static final TypeToken<TestClass> testClassTypeToken = TypeToken.of(TestClass.class);
  private static final TypeToken<TestClass2> testClass2TypeToken = TypeToken.of(TestClass2.class);
  private static final DependencyKey<TestInterface> testInterfaceDependencyKey = new DependencyKey<>(testInterfaceTypeToken, null);
  private static final DependencyKey<TestClass> testClassDependencyKey = new DependencyKey<>(testClassTypeToken, null);

  @Mock RealObjectMapping mParentMap;

  private final RealObjectMapping mRealObjectMapping = new RealObjectMapping();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSimplePutAndGet() {
    mRealObjectMapping.put(testClassDependencyKey, true);

    boolean containsKey = mRealObjectMapping.containsKey(testClassDependencyKey);
    TypeToken implementationToken = mRealObjectMapping.getImplementation(testClassDependencyKey);
    boolean shouldMap = mRealObjectMapping.shouldMapDependency(testClassDependencyKey);

    assertThat(containsKey).isTrue();
    assertThat(implementationToken).isEqualTo(testClassTypeToken);
    assertThat(shouldMap).isTrue();
  }

  @Test
  public void testCustomPutAndGet() {
    mRealObjectMapping.put(testInterfaceDependencyKey, testClassTypeToken, false);

    boolean containsKey = mRealObjectMapping.containsKey(testInterfaceDependencyKey);
    TypeToken implementationToken = mRealObjectMapping.getImplementation(testInterfaceDependencyKey);
    boolean shouldMap = mRealObjectMapping.shouldMapDependency(testInterfaceDependencyKey);

    assertThat(containsKey).isTrue();
    assertThat(implementationToken).isEqualTo(testClassTypeToken);
    assertThat(shouldMap).isFalse();
  }

  @Test
  public void testParentCalledWhenDepMissing() {
    mRealObjectMapping.setParentMap(mParentMap);

    boolean containsKey = mRealObjectMapping.containsKey(testClassDependencyKey);
    TypeToken implementationToken = mRealObjectMapping.getImplementation(testClassDependencyKey);
    boolean shouldMap = mRealObjectMapping.shouldMapDependency(testClassDependencyKey);

    assertThat(containsKey).isFalse();
    assertThat(implementationToken).isNull();
    assertThat(shouldMap).isFalse();

    InOrder inOrder = Mockito.inOrder(mParentMap);
    inOrder.verify(mParentMap).containsKey(testClassDependencyKey);
    inOrder.verify(mParentMap).getImplementation(testClassDependencyKey);
    inOrder.verify(mParentMap).shouldMapDependency(testClassDependencyKey);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testParentNotCalledWhenDepExists() {
    mRealObjectMapping.setParentMap(mParentMap);
    mRealObjectMapping.put(testClassDependencyKey, true);

    boolean containsKey = mRealObjectMapping.containsKey(testClassDependencyKey);
    TypeToken implementationToken = mRealObjectMapping.getImplementation(testClassDependencyKey);
    boolean shouldMap = mRealObjectMapping.shouldMapDependency(testClassDependencyKey);

    assertThat(containsKey).isTrue();
    assertThat(implementationToken).isEqualTo(testClassTypeToken);
    assertThat(shouldMap).isTrue();
    verifyNoMoreInteractions(mParentMap);
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testCantOverwrite() {
    mRealObjectMapping.put(testInterfaceDependencyKey, testClassTypeToken, true);
    mRealObjectMapping.put(testInterfaceDependencyKey, testClass2TypeToken, true);
  }

  @Test
  public void testCanOverwriteParent() {
    RealObjectMapping parent = new RealObjectMapping();
    parent.put(testInterfaceDependencyKey, testClassTypeToken, false);
    mRealObjectMapping.setParentMap(parent);
    mRealObjectMapping.put(testInterfaceDependencyKey, testClass2TypeToken, true);

    boolean containsKey = mRealObjectMapping.containsKey(testInterfaceDependencyKey);
    TypeToken implementationToken = mRealObjectMapping.getImplementation(testInterfaceDependencyKey);
    boolean shouldMap = mRealObjectMapping.shouldMapDependency(testInterfaceDependencyKey);

    assertThat(containsKey).isTrue();
    assertThat(implementationToken)
        .isEqualTo(testClass2TypeToken)
        .isNotEqualTo(testClassTypeToken);
    assertThat(shouldMap).isTrue();
  }
}
