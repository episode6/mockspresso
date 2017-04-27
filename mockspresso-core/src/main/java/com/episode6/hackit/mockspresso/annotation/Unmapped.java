package com.episode6.hackit.mockspresso.annotation;

import java.lang.annotation.*;

/**
 * Field annotation for mocks that tells mockspresso NOT to map the value contained therein.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unmapped {
}
