package com.episode6.hackit.mockspresso.testobject;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import org.mockito.Mock;

/**
 * Sample object to use in tests.
 */
public class SubclassTestObject extends SuperclassTestObject {
  @RealObject String subclassString = "subclass";
  @Mock SubClassInnerClass subclassInnterClass;

  public static class SubClassInnerClass {}
}
