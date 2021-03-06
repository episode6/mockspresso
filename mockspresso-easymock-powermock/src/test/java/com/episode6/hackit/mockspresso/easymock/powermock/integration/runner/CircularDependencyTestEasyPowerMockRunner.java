package com.episode6.hackit.mockspresso.easymock.powermock.integration.runner;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.exception.CircularDependencyError;
import com.episode6.hackit.mockspresso.testing.testobjects.CircularDependencies;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectByJavaxConfig;
import static com.episode6.hackit.mockspresso.easymock.powermock.MockspressoEasyPowerMockPluginsJavaSupport.mockByPowerMock;

/**
 * Tests {@link CircularDependencies}
 */
@RunWith(PowerMockRunner.class)
public class CircularDependencyTestEasyPowerMockRunner {

  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
      .plugin(injectByJavaxConfig())
      .plugin(mockByPowerMock())
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
    testObject.mC.mA.get().mB.get().mC.get();
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
