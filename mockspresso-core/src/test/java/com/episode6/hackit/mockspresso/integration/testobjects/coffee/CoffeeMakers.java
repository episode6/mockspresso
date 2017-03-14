package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Different implementations of a coffee maker to use in integration tests
 */
public class CoffeeMakers {

  /**
   * Internal implementation of brew method so I don't have to re-write it for every test class
   */
  private static Coffee brewInternal(Pump pump, Heater heater, CoffeeGrounds coffeeGrounds) {
    Water water = pump.pump();
    heater.heat(water);
    return new Coffee(water, coffeeGrounds);
  }

  /**
   * The most basic coffee maker, can be created via the simple inject plugin
   */
  public static class SimpleCoffeeMaker implements CoffeeMaker {
    private final Pump mPump;
    private final Heater mHeater;
    private final CoffeeGrounds mCoffeeGrounds;

    public SimpleCoffeeMaker(Pump pump, Heater heater, CoffeeGrounds coffeeGrounds) {
      mPump = pump;
      mHeater = heater;
      mCoffeeGrounds = coffeeGrounds;
    }

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGrounds);
    }
  }

  public static class FieldInjectedCoffeeMakerWithGroundsProvider implements CoffeeMaker {
    @Inject Pump mPump;
    @Inject Heater mHeater;
    @Inject Provider<CoffeeGrounds> mCoffeeGroundsProvider;

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGroundsProvider.get());
    }
  }
}
