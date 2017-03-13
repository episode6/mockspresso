package com.episode6.hackit.mockspresso.testobject;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * classes used in integration tests
 */
public class CoffeeExample {

  public interface Heater {
    void heat();
  }

  public interface Pump {
    void pump();
  }

  public static class CoffeeMaker {
    @Inject Provider<Heater> mHeaterProvider;
    @Inject Pump mPump;

    public void brew() {
      mHeaterProvider.get().heat();
      mPump.pump();
    }
  }
}
