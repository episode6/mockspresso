package com.episode6.hackit.mockspresso.reflect;

import javax.inject.Named;

/**
 * A convenience annotation literal for the @Named annotation.
 */
public class NamedAnnotationLiteral extends AnnotationLiteral<Named> implements Named {
  private final String mValue;

  public NamedAnnotationLiteral() {
    this("");
  }

  public NamedAnnotationLiteral(String value) {
    mValue = value;
  }

  @Override
  public String value() {
    return mValue;
  }
}
