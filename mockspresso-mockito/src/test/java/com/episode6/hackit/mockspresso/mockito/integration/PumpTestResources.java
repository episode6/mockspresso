package com.episode6.hackit.mockspresso.mockito.integration;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Water;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin;
import org.junit.Before;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Separate resources class that could be shared by multiple tests
 */
public class PumpTestResources {

  @Mock Pump mPump;
  @Mock Water mWater;

  // Because the @Rule we're using is based on javaxInjection, we can define all 4 injected
  // coffee makers here and test those.
  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;
  @RealObject CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider mFieldInjectedCoffeeMakerWithGroundsProvider;
  @RealObject CoffeeMakers.MethodInjectedCoffeeMaker mMethodInjectedCoffeeMaker;
  @RealObject CoffeeMakers.MixedInjectionCoffeeMaker mMixedInjectionCoffeeMaker;
  /* RealObject */ CoffeeMakers.SimpleCoffeeMaker mSimpleCoffeeMaker; // we construct this from a sub-instance of mockspresso

  @Before
  public void setup(Mockspresso mockspresso) {
    when(mPump.pump()).thenReturn(mWater);

    mSimpleCoffeeMaker = mockspresso.buildUpon()
        .injector().simple()
        .build().create(CoffeeMakers.SimpleCoffeeMaker.class);
  }

  public void verifyInteractionsAndCoffeeObject(Coffee coffee) {
    // verify our defined pump mock was called;
    verify(mPump).pump();

    // assert that coffee was actually created contains the Water object we expect
    assertThat(coffee)
        .isNotNull()
        .isNot(mockCondition());
    assertThat(coffee.getWater())
        .isNotNull()
        .isEqualTo(mWater);

    // coffee grounds should be a mock even though we never defined one.
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(mockCondition());
  }
}
