package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.exception.MultipleQualifierAnnotationException;
import com.episode6.hackit.mockspresso.reflect.NamedAnnotationLiteral;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link DependencyKey}
 */
@RunWith(DefaultTestRunner.class)
public class DependencyKeyTest {

  @Named("test1") Integer testProp1;
  @Named HashMap<String, Provider<Integer>> testProp2;
  @Named @RealObject HashMap<String, Provider<Integer>> testProp3;
  @RealObject @Named HashMap<String, Provider<String>> testProp4;
  @RealObject @Named("test1") Integer testProp5;

  @Named @RealObject @TestQualifierAnnotation String badTestProp;


  @Test
  public void testKeyEquality() {
    DependencyKey key1 = DependencyKey.fromField(prop("testProp1"));
    DependencyKey key2 = DependencyKey.fromField(prop("testProp2"));
    DependencyKey key3 = DependencyKey.fromField(prop("testProp3"));
    DependencyKey key4 = DependencyKey.fromField(prop("testProp4"));
    DependencyKey key5 = DependencyKey.fromField(prop("testProp5"));

    assertThat(key1)
        .isEqualTo(key5)
        .isNotEqualTo(key2)
        .isNotEqualTo(key4);
    assertThat(key2)
        .isEqualTo(key3)
        .isNotEqualTo(key4)
        .isNotEqualTo(key5);
  }

  @Test(expected = MultipleQualifierAnnotationException.class)
  public void testMultipleQualifiersFailure() {
    DependencyKey.fromField(prop("badTestProp"));
  }

  @Test
  public void testCustomKeyEquality() {
    DependencyKey key1 = DependencyKey.fromField(prop("testProp1"));
    DependencyKey customKey1 = new DependencyKey<>(
        TypeToken.of(Integer.class),
        new NamedAnnotationLiteral("test1"));
    DependencyKey key2 = DependencyKey.fromField(prop("testProp2"));
    DependencyKey customKey2 = new DependencyKey<>(
        new TypeToken<HashMap<String, Provider<Integer>>>() {},
        new NamedAnnotationLiteral());

    assertThat(key1)
        .isEqualTo(customKey1)
        .isNotEqualTo(customKey2);

    assertThat(key2)
        .isEqualTo(customKey2)
        .isNotEqualTo(customKey1);
  }

  private static Field prop(String name) {
    try {
      return DependencyKeyTest.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }
}
