package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.mockito.MockspressoMockitoConfig;
import com.episode6.hackit.mockspresso.testobject.CoffeeExample.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Integration test for Mockspresso usage with mockito
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoMockitoIntegrationTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .mockerConfig(new MockspressoMockitoConfig())
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
