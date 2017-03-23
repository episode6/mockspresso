package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

import com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectionConfig;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Different implementations of a coffee maker to use in integration tests. Every coffee maker
 * uses the same implementation of brew() so we can keep these tests simple.
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
   * The most basic coffee maker, can be created via the {@link SimpleInjectMockspressoPlugin}
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

    public Heater getHeater() {
      return mHeater;
    }
  }

  /**
   * A coffee make that is created via constructor injection
   */
  public static class ConstructorInjectedCofferMaker implements CoffeeMaker {
    private final Pump mPump;
    private final Heater mHeater;
    private final CoffeeGrounds mCoffeeGrounds;

    @Inject
    public ConstructorInjectedCofferMaker(Pump pump, Heater heater, CoffeeGrounds coffeeGrounds) {
      mPump = pump;
      mHeater = heater;
      mCoffeeGrounds = coffeeGrounds;
    }

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGrounds);
    }
  }

  /**
   * A coffee maker that is created via Field injection. It also expects
   * a {@link Provider<CoffeeGrounds>} instead of a {@link CoffeeGrounds} object.
   * This provider can be handled automatically via the {@link JavaxInjectMockspressoPlugin}.
   */
  public static class FieldInjectedCoffeeMakerWithGroundsProvider implements CoffeeMaker {
    @Inject Pump mPump;
    @Inject Heater mHeater;
    @Inject Provider<CoffeeGrounds> mCoffeeGroundsProvider;

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGroundsProvider.get());
    }
  }

  /**
   * A coffee maker that is created via method injection.
   */
  public static class MethodInjectedCoffeeMaker implements CoffeeMaker {
    private Pump mPump;
    private Heater mHeater;
    private CoffeeGrounds mCoffeeGrounds;

    @Inject
    public final void injectMethodInjectedCoffeeMaker(Pump pump, Heater heater, CoffeeGrounds coffeeGrounds) {
      mPump = pump;
      mHeater = heater;
      mCoffeeGrounds = coffeeGrounds;
    }

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGrounds);
    }
  }

  /**
   * A coffee make that is created via constructor injection, method injection
   * and field injection.
   *
   * This probably wouldn't be the smartest way to write a class irl.
   */
  public static class MixedInjectionCoffeeMaker implements CoffeeMaker {
    private final Pump mPump;
    private Heater mHeater;
    @Inject Provider<CoffeeGrounds> mCoffeeGroundsProvider;

    @Inject
    public MixedInjectionCoffeeMaker(Pump pump) {
      mPump = pump;
    }

    @Inject
    public final void injectMixedInjectionCoffeeMaker(Heater heater) {
      mHeater = heater;
    }

    @Override
    public Coffee brew() {
      return brewInternal(mPump, mHeater, mCoffeeGroundsProvider.get());
    }
  }
}
