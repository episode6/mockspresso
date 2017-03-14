package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.*;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration test for Mockspresso usage with mockito
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoMockitoIntegrationTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.simple()
      .plugin(MockitoPlugin.getInstance())
      .buildRule();

  @Mock Water mWater;
  @Mock Heater mHeater;
  @Mock Pump mPump;

  @RealObject CoffeeMakers.SimpleCoffeeMaker mCoffeeMaker;

  @Test
  public void placeholderTest() {
    when(mPump.pump()).thenReturn(mWater);

    Coffee coffee = mCoffeeMaker.brew();

    verify(mPump).pump();
    verify(mHeater).heat(mWater);
    assertThat(coffee).isNot(mockitoMock());
    assertThat(coffee.getWater()).isEqualTo(mWater);
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(mockitoMock());
  }
}
