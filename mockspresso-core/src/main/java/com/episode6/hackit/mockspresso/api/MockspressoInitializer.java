package com.episode6.hackit.mockspresso.api;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * An initialization interface. Enables to use of shared test
 * resources that require custom setup logic. Add a custom
 * initializer via the {@link Mockspresso.Builder}
 */
public interface MockspressoInitializer {

  /**
   * The setup method is executed after field injection as a final step when creating a mockspresso
   * instance.
   * @param mockspresso The mockspresso instance that this initializer was added to, fully configured.
   */
  void setup(Mockspresso mockspresso);
}
