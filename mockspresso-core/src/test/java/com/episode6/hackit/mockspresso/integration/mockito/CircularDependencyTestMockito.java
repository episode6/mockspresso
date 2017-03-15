package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.integration.testobjects.CircularDependencies;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests {@link CircularDependencies}
 */
@RunWith(JUnit4.class)
public class CircularDependencyTestMockito {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .buildRule();

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyError() {
    TestObject testObject = new TestObject();
    mockspresso.buildUpon()
        .fieldsFrom(testObject)
        .build();
  }

  private static class TestObject {
    @RealObject CircularDependencies.A mA;
    @RealObject CircularDependencies.B mB;
    @RealObject CircularDependencies.C mC;
  }
}
