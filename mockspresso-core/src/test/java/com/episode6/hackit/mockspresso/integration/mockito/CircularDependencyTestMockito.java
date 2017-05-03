package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.BuildMockspresso;
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

  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .buildRule();

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorFields() {
    TestObject testObject = new TestObject();
    mockspresso.buildUpon()
        .testResources(testObject)
        .build();
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorDynamic() {
    CircularDependencies.C c = mockspresso.buildUpon()
        .realObject(CircularDependencies.A.class)
        .realObject(CircularDependencies.B.class)
        .build()
        .create(CircularDependencies.C.class);
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorFieldsProvider() {
    TestObjectProviders testObject = new TestObjectProviders();
    mockspresso.buildUpon()
        .testResources(testObject)
        .build();
    testObject.mA.mB.get().mC.get().mA.get();
  }

  @Test(expected = CircularDependencyError.class)
  public void checkForCircularDependencyErrorDynamicProvider() {
    CircularDependencies.CProvider c = mockspresso.buildUpon()
        .realObject(CircularDependencies.AProvider.class)
        .realObject(CircularDependencies.BProvider.class)
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
