package com.episode6.hackit.mockspresso.reflect.testobject;

import com.episode6.hackit.mockspresso.reflect.annotation.TestSimpleAnnotation;
import org.mockito.Mock;

/**
 * Sample object to use in tests.
 */
public class SubclassTestObject extends SuperclassTestObject {
  @TestSimpleAnnotation String subclassString = "subclass";
  @Mock SubClassInnerClass subclassInnterClass;

  public static class SubClassInnerClass {}
}
