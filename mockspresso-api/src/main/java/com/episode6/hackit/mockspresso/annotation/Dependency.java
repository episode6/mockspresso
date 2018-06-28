package com.episode6.hackit.mockspresso.annotation;

import java.lang.annotation.*;

/**
 * Annotation that represents a dependency to be added to mockspresso's dependency map.
 * Applies to non-null (usually final) fields.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {

  /**
   * @return Class this object should be bound as in the dependency map (if different than the fielc declaration)
   */
  Class<?> bindAs() default Dependency.class;
}
