package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

import javax.inject.Named;

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

  public static class NamedHeater implements Heater {

    private final String mName;

    public NamedHeater(@Named("heater_name") String name) {
      mName = name;
    }

    public String getName() {
      return mName;
    }

    @Override
    public void heat(Water water) {
      // heat water
    }
  }

  public static class NumberedHeater implements Heater {

    private final int mNumber;

    public NumberedHeater(@Named("heater_number") int number) {
      mNumber = number;
    }

    public int getNumber() {
      return mNumber;
    }

    @Override
    public void heat(Water water) {
      // heat water
    }
  }

  public static class NumberedArrayHeater implements Heater {

    private final int[] mNumbers;

    public NumberedArrayHeater(@Named("heater_numbers") int[] numbers) {
      mNumbers = numbers;
    }

    public int[] getNumbers() {
      return mNumbers;
    }

    @Override
    public void heat(Water water) {
      // heat water
    }
  }
}
