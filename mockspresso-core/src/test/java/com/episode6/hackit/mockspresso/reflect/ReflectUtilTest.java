package com.episode6.hackit.mockspresso.reflect;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.exception.MultipleQualifierAnnotationException;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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

  private Field prop(String name) {
    try {
      return getClass().getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
