package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.mockito.MockspressoMockitoPlugin;
import com.episode6.hackit.mockspresso.testobject.CoffeeExample.CoffeeMaker;
import com.episode6.hackit.mockspresso.testobject.CoffeeExample.Heater;
import com.episode6.hackit.mockspresso.testobject.CoffeeExample.Pump;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

/**
 * Integration test for Mockspresso usage with mockito
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoMockitoIntegrationTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockspressoMockitoPlugin.getInstance())
      .buildRule();

  @Mock Heater mHeater;
  @Mock Pump mPump;

  @RealObject CoffeeMaker mCoffeeMaker;

  @Test
  public void placeholderTest() {
    mCoffeeMaker.brew();

    verify(mHeater).heat();
    verify(mPump).pump();
  }
}
