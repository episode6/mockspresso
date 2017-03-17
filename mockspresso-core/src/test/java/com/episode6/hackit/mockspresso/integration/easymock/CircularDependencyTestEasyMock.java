package com.episode6.hackit.mockspresso.integration.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.easymock.EasyMockPlugin;
import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.integration.testobjects.CircularDependencies;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests {@link CircularDependencies}
 */
@RunWith(JUnit4.class)
public class CircularDependencyTestEasyMock {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(EasyMockPlugin.getInstance())
      .buildRule();

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorFields() {
    TestObject testObject = new TestObject();
    mockspresso.buildUpon()
        .fieldsFrom(testObject)
        .build();
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorDynamic() {
    CircularDependencies.C c = mockspresso.buildUpon()
        .useRealObject(CircularDependencies.A.class)
        .useRealObject(CircularDependencies.B.class)
        .build()
        .create(CircularDependencies.C.class);
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorFieldsProvider() {
    TestObjectProviders testObject = new TestObjectProviders();
    mockspresso.buildUpon()
        .fieldsFrom(testObject)
        .build();
    testObject.mC.mA.get().mB.get().mC.get();
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorDynamicProvider() {
    CircularDependencies.CProvider c = mockspresso.buildUpon()
        .useRealObject(CircularDependencies.AProvider.class)
        .useRealObject(CircularDependencies.BProvider.class)
        .build()
        .create(CircularDependencies.CProvider.class);
    c.mA.get().mB.get().mC.get();
  }

  private static class TestObject {
    @RealObject CircularDependencies.A mA;
    @RealObject CircularDependencies.B mB;
    @RealObject CircularDependencies.C mC;
  }

  private static class TestObjectProviders {
    @RealObject CircularDependencies.AProvider mA;
    @RealObject CircularDependencies.BProvider mB;
    @RealObject CircularDependencies.CProvider mC;
  }
}
