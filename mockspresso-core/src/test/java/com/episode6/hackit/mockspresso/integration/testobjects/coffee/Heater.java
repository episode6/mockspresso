package com.episode6.hackit.mockspresso.integration.testobjects.coffee;

/**
 * a thing that heats water
 */
public interface Heater {
  Water heat(Water coldWater);
}
