package com.episode6.hackit.mockspresso.mockito.integration;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.Dependency;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.TestQualifierAnnotation;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.*;
import org.junit.Rule;
import org.junit.Test;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectBySimpleConfig;
import static com.episode6.hackit.mockspresso.mockito.MockspressoMockitoPluginsJavaSupport.mockByMockito;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests importing fields annotated with {@link Dependency}
 */
public class DependencyImportTestMockito {
  @Rule public final Mockspresso.Rule simpleMockspresso = BuildMockspresso.with()
      .plugin(injectBySimpleConfig())
      .plugin(mockByMockito())
      .buildRule();

  @Dependency(bindAs = Pump.class) CoffeeMakerComponents.RealWaterPump mRealPump = new CoffeeMakerComponents.RealWaterPump();
  @Dependency Heater mRealHeater = new CoffeeMakerComponents.RealHeater();
  @Dependency(bindAs = Water.class) @TestQualifierAnnotation CoffeeMakerComponents.RealWater mRealWater = new CoffeeMakerComponents.RealWater();

  @RealObject CoffeeMakers.SimpleCoffeeMaker mCoffeeMaker;
  @RealObject TestClass mTestClass;

  @Test
  public void testComponentsImported() {
    assertThat(mCoffeeMaker.getHeater()).isEqualTo(mRealHeater);
    assertThat(mCoffeeMaker.getPump()).isEqualTo(mRealPump);
    assertThat(mTestClass.mWater).isEqualTo(mRealWater);
  }

  static class TestClass {
    final Water mWater;

    TestClass(@TestQualifierAnnotation Water water) {
      mWater = water;
    }
  }
}
