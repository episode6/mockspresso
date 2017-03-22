package com.episode6.hackit.mockspresso.annotation;

import java.lang.annotation.*;

/**
 * Annotation that represents a real object used in a test (as opposed to a mock)
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RealObject {

  /**
   * Class to be used for implementation of this object.
   */
  Class<?> implementation() default RealObject.class;
}
