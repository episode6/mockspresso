package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.annotation.Dependency;
import com.episode6.hackit.mockspresso.reflect.AnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.testing.testobjects.TestQualifierAnnotation;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link DependencyFieldKeyAdjuster}
 */
public class DependencyFieldKeyAdjusterTest {
  public static class TestQualifierAnnotationLiteral extends AnnotationLiteral<TestQualifierAnnotation> implements TestQualifierAnnotation {}

  DependencyKey<TestInterface> interfaceKey = DependencyKey.of(TestInterface.class);
  DependencyKey<TestClass> classKey = DependencyKey.of(TestClass.class);
  DependencyKey<TestInterface> annotatedInterfaceKey = DependencyKey.of(TestInterface.class, new TestQualifierAnnotationLiteral());
  DependencyKey<TestClass> annotatedClassKey = DependencyKey.of(TestClass.class, new TestQualifierAnnotationLiteral());


  @Dependency TestClass testClass = new TestClass();
  @Dependency(bindAs = TestInterface.class) TestClass testClassAsInterface = new TestClass();

  @Dependency @TestQualifierAnnotation TestClass annotatedTestClass = new TestClass();
  @Dependency(bindAs = TestInterface.class) @TestQualifierAnnotation TestClass annotatedTestClassAsInterface = new TestClass();

  DependencyFieldKeyAdjuster mKeyAdjuster = new DependencyFieldKeyAdjuster();

  @Test
  public void testTestClass() throws NoSuchFieldException {
    DependencyKey result = testField("testClass");
    assertThat(result).isEqualTo(classKey);
  }

  @Test
  public void testTestClassAsInterface() throws NoSuchFieldException {
    DependencyKey result = testField("testClassAsInterface");
    assertThat(result).isEqualTo(interfaceKey);
  }

  @Test
  public void testAnnotatedTestClass() throws NoSuchFieldException {
    DependencyKey result = testField("annotatedTestClass");
    assertThat(result).isEqualTo(annotatedClassKey);
  }

  @Test
  public void testAnnotatedTestClassAsInterface() throws NoSuchFieldException {
    DependencyKey result = testField("annotatedTestClassAsInterface");
    assertThat(result).isEqualTo(annotatedInterfaceKey);
  }

  private DependencyKey testField(String fieldName) throws NoSuchFieldException {
    Field field = getClass().getDeclaredField(fieldName);
    DependencyKey fieldKey = DependencyKey.fromField(field);
    return mKeyAdjuster.adjustKey(fieldKey, field);
  }

  interface TestInterface {}
  static class TestClass implements TestInterface {}
}
