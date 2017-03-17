package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.testobject.SubclassTestObject;
import com.episode6.hackit.mockspresso.testobject.SuperclassTestObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;

/**
 * Tests {@link DependencyValidator}
 */
@RunWith(DefaultTestRunner.class)
public class DependencyValidatorTest {

  @Test
  public void testBaseValidator() {
    createBaseValidator();
    // no exception == pass
  }

  @Test(expected = CircularDependencyError.class)
  public void testBadValidator() {
    createBadValidator();
  }

  @Test
  public void testWorkingAppend() {
    createValidatorToAppendTo().append(createBaseValidator());
    // no exception == pass
  }

  @Test(expected = CircularDependencyError.class)
  public void testFailedAppend() {
    createBadValidatorToAppendTo().append(createBaseValidator());
  }

  private static DependencyValidator createBaseValidator() {
    DependencyValidator topLevel = new DependencyValidator(DependencyKey.of(TestClass.class));
    topLevel.child(DependencyKey.of(String.class));
    topLevel.child(DependencyKey.of(Integer.class));
    DependencyValidator secondLevel = topLevel.child(DependencyKey.of(Iterator.class));
    secondLevel.child(DependencyKey.of(new TypeToken<Iterable<String>>() {}));
    secondLevel.child(DependencyKey.of(String.class)); // not a direct descendant of the other string.
    return topLevel;
  }

  private static DependencyValidator createBadValidator() {
    DependencyValidator topLevel = new DependencyValidator(DependencyKey.of(TestClass.class));
    topLevel.child(DependencyKey.of(String.class));
    topLevel.child(DependencyKey.of(Integer.class));
    DependencyValidator secondLevel = topLevel.child(DependencyKey.of(Iterator.class));
    DependencyValidator thirdLevel = secondLevel.child(DependencyKey.of(new TypeToken<Iterable<String>>() {}));
    thirdLevel.child(DependencyKey.of(TestClass.class));
    return topLevel;
  }

  private static DependencyValidator createValidatorToAppendTo() {
    DependencyValidator topLevel = new DependencyValidator(DependencyKey.of(Exception.class));
    return topLevel
        .child(DependencyKey.of(SubclassTestObject.class))
        .child(DependencyKey.of(SuperclassTestObject.class))
        .child(DependencyKey.of(TestClass.class));
  }

  private static DependencyValidator createBadValidatorToAppendTo() {
    DependencyValidator topLevel = new DependencyValidator(DependencyKey.of(new TypeToken<Iterable<String>>() {}));
    return topLevel
        .child(DependencyKey.of(SubclassTestObject.class))
        .child(DependencyKey.of(SuperclassTestObject.class))
        .child(DependencyKey.of(TestClass.class));
  }

  public static class TestClass {}
}
