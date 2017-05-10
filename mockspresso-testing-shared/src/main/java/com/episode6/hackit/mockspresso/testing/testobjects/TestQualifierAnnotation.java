package com.episode6.hackit.mockspresso.testing.testobjects;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that represents a real object used in a test (as opposed to a mock)
 */
@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface TestQualifierAnnotation {
}
