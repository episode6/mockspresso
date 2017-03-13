package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

/**
 * coffee = water + coffee grounds
 */
public class Coffee {
  private final Water mWater;
  private final CoffeeGrounds mCoffeeGrounds;

  public Coffee(Water water, CoffeeGrounds coffeeGrounds) {
    mWater = water;
    mCoffeeGrounds = coffeeGrounds;
  }

  public Water getWater() {
    return mWater;
  }

  public CoffeeGrounds getCoffeeGrounds() {
    return mCoffeeGrounds;
  }
}
