package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.exception.MultipleQualifierAnnotationException;
import com.episode6.hackit.mockspresso.testobject.SubclassTestObject;
import com.episode6.hackit.mockspresso.testobject.SuperclassTestObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link ReflectUtil}
 */
@RunWith(DefaultTestRunner.class)
public class ReflectUtilTest {

  @Named("test1") String testProp1;
  @Named String testProp2;
  @Named @RealObject String testProp3;
  @RealObject @TestQualifierAnnotation Integer testProp4;
  @RealObject @Named("test1") Integer testProp5;

  @Named @RealObject @TestQualifierAnnotation String badTestProp;

  @Test
  public void testFindQualifierAnnotation() throws IllegalAccessException, InstantiationException {
    Annotation found1 = ReflectUtil.findQualifierAnnotation(prop("testProp1"));
    Annotation found2 = ReflectUtil.findQualifierAnnotation(prop("testProp2"));
    Annotation found3 = ReflectUtil.findQualifierAnnotation(prop("testProp3"));
    Annotation found4 = ReflectUtil.findQualifierAnnotation(prop("testProp4"));
    Annotation found5 = ReflectUtil.findQualifierAnnotation(prop("testProp5"));

    assertTrue(found1.annotationType() == Named.class);
    assertTrue(found2.annotationType() == Named.class);
    assertTrue(found3.annotationType() == Named.class);
    assertTrue(found4.annotationType() == TestQualifierAnnotation.class);
    assertTrue(found5.annotationType() == Named.class);

    assertThat(found1)
        .isNotEqualTo(found2)
        .isEqualTo(found5);
    assertThat(found2)
        .isNotEqualTo(found1)
        .isEqualTo(found3);
    assertThat(((Named)found1).value()).isEqualTo("test1");
  }

  @Test(expected = MultipleQualifierAnnotationException.class)
  public void testMultipleQualifiersFailure() {
    ReflectUtil.findQualifierAnnotation(prop("badTestProp"));
  }

  @Test
  public void testGetAllDeclaredFields() {
    List<Field> fields = ReflectUtil.getAllDeclaredFields(SubclassTestObject.class);

    assertThat(fields).hasSize(4);
    assertFieldInList(fields, "subclassString", String.class);
    assertFieldInList(fields, "superclassTestString", String.class);
    assertFieldInList(fields, "subclassInnterClass", SubclassTestObject.SubClassInnerClass.class);
    assertFieldInList(fields, "superclassInnerClass", SuperclassTestObject.SuperClassInnerClass.class);
  }

  @Test
  public void testIsAnyAnnotationPresent() {
    Field field = prop("testProp4");

    boolean result1 = ReflectUtil.isAnyAnnotationPresent(field, Arrays.asList(Spy.class, TestQualifierAnnotation.class, Mock.class));
    boolean result2 = ReflectUtil.isAnyAnnotationPresent(field, Arrays.asList(Spy.class, Mock.class));
    boolean result3 = ReflectUtil.isAnyAnnotationPresent(field, Arrays.asList(Mock.class, RealObject.class));

    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
    assertThat(result3).isTrue();
  }

  @Test
  public void testGetAllMethodsOnSubclass() {
    List<Method> methodList = ReflectUtil.getAllDeclaredMethods(SubClass.class);

    assertThat(methodList).hasSize(14);
    // first 12 methods should come from Object
    for (int i = 0; i < 11; i++) {
      assertTrue(methodList.get(i).getDeclaringClass() == Object.class);
    }
    // then the superclass method
    assertThat(methodList.get(12).getName()).isEqualTo("doSomething");
    //then the subclass method
    assertThat(methodList.get(13).getName()).isEqualTo("doSomethingElse");
  }

  @Test
  public void testIsAnyAnnotationPresentMethod() throws NoSuchMethodException {
    Method method = ClassWithMethodWithAnnotation.class.getDeclaredMethod("doNothing");

    boolean result1 = ReflectUtil.isAnyAnnotationPresent(method, Arrays.asList(Spy.class, TestQualifierAnnotation.class, Mock.class));
    boolean result2 = ReflectUtil.isAnyAnnotationPresent(method, Arrays.asList(Spy.class, Mock.class));
    boolean result3 = ReflectUtil.isAnyAnnotationPresent(method, Arrays.asList(Mock.class, Inject.class));

    assertThat(result1).isTrue();
    assertThat(result2).isFalse();
    assertThat(result3).isTrue();
  }

  private void assertFieldInList(List<Field> fieldList, String name, Class<?> clazz) {
    for (Field field : fieldList) {
      if (field.getName().equals(name) && field.getGenericType() == clazz) {
        return;
      }
    }
    throw new AssertionError("Expected to find field named " + name + " of type " + clazz.getSimpleName() + ", but did not.");
  }

  private Field prop(String name) {
    try {
      return getClass().getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public static class SuperClass {
    public void doSomething() {}
    public void doSomethingElse(List<String> stringList) {}
  }

  public static class SubClass extends SuperClass {

    @Override
    public void doSomethingElse(List stringList) {}
  }

  public static class ClassWithMethodWithAnnotation {
    @Inject @TestQualifierAnnotation public void doNothing() {}
  }
}
