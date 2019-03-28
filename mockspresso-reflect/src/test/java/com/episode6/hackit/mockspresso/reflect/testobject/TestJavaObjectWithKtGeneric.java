package com.episode6.hackit.mockspresso.reflect.testobject;

import java.lang.reflect.Field;

/**
 *
 */
public class TestJavaObjectWithKtGeneric {

  public TestGenericKtInterface<String> genericIface;

  public Field getGenericIfaceField() {
    try {
      return getClass().getDeclaredField("genericIface");
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

}
