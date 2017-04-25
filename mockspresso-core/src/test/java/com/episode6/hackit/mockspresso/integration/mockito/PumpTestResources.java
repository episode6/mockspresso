package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.MockspressoInitializer;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Water;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Separate resources class that could be shared by multiple tests
 */
public class PumpTestResources implements MockspressoInitializer {

  @Mock Pump mPump;
  @Mock Water mWater;

  // Because the @Rule we're using is based on javaxInjection, we can define all 4 injected
  // coffee makers here and test those.
  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;
  @RealObject CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider mFieldInjectedCoffeeMakerWithGroundsProvider;
  @RealObject CoffeeMakers.MethodInjectedCoffeeMaker mMethodInjectedCoffeeMaker;
  @RealObject CoffeeMakers.MixedInjectionCoffeeMaker mMixedInjectionCoffeeMaker;
  /* RealObject */ CoffeeMakers.SimpleCoffeeMaker mSimpleCoffeeMaker; // we construct this from a sub-instance of mockspresso

  @Override
  public void setup(Mockspresso mockspresso) {
    when(mPump.pump()).thenReturn(mWater);

    mSimpleCoffeeMaker = mockspresso.buildUpon()
        .plugin(SimpleInjectMockspressoPlugin.getInstance())
        .build().create(CoffeeMakers.SimpleCoffeeMaker.class);
  }

  public void verifyInteractionsAndCoffeeObject(Coffee coffee) {
    // verify our defined pump mock was called;
    verify(mPump).pump();

    // assert that coffee was actually created contains the Water object we expect
    assertThat(coffee)
        .isNotNull()
        .isNot(mockitoMock());
    assertThat(coffee.getWater())
        .isNotNull()
        .isEqualTo(mWater);

    // coffee grounds should be a mock even though we never defined one.
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(mockitoMock());
  }
}
