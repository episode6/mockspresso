package com.episode6.hackit.mockspresso.reflect.testobject;

import java.lang.reflect.Field;

/**
 * Test object in java that mis-uses a kotlin interface.
 */
public class TestJavaObjectWithKtGeneric {

  // this field is basically a mis-use of the `TestGenericKtInterface` because of the `out` keyword on
  // the type variable in that class. It should read `TestGenericKtInterface<? extends String>`
  public TestGenericKtInterface<String> genericIface;

  public Field getGenericIfaceField() {
    try {
      return getClass().getDeclaredField("genericIface");
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

}
