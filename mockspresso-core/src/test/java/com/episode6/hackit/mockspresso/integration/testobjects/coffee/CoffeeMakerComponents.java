package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

/**
 * Some real coffee maker components to build an integration test with.
 */
public class CoffeeMakerComponents {

  public static class RealWater implements Water {}

  public static class RealWaterPump implements Pump {
    @Override
    public Water pump() {
      return new RealWater();
    }
  }

  public static class RealHeater implements Heater {

    @Override
    public void heat(Water water) {
      // heats the water
    }
  }

  public static class RealCoffeeGrounds implements CoffeeGrounds {}
}
