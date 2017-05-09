package com.episode6.hackit.mockspresso.testobject;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.TestQualifierAnnotation;
import org.mockito.Mock;

/**
 * Sample object to use in tests
 */
public class SuperclassTestObject {
  @RealObject @TestQualifierAnnotation String superclassTestString = "superclass";
  @Mock SuperClassInnerClass superclassInnerClass;

  public static class SuperClassInnerClass {}
}
