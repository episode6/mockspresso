package com.episode6.hackit.mockspresso.testing.testobjects.coffee;

/**
 * A factory that creates named coffeeGrounds
 */
public class CoffeeGroundsFactory {

  public CoffeeGrounds createNamedCoffeeGrounds(String name) {
    return new NamedCoffeeGrounds(name);
  }

  public static class NamedCoffeeGrounds implements CoffeeGrounds {
    public final String name;

    public NamedCoffeeGrounds(String name) {
      this.name = name;
    }
  }
}
