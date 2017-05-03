package com.episode6.hackit.mockspresso.reflect.testobject;

import com.episode6.hackit.mockspresso.reflect.annotation.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.reflect.annotation.TestSimpleAnnotation;
import org.mockito.Mock;

/**
 * Sample object to use in tests
 */
public class SuperclassTestObject {
  @TestSimpleAnnotation @TestQualifierAnnotation String superclassTestString = "superclass";
  @Mock SuperClassInnerClass superclassInnerClass;

  public static class SuperClassInnerClass {}
}
