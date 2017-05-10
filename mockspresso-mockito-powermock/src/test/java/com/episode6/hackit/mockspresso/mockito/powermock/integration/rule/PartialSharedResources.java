package com.episode6.hackit.mockspresso.mockito.powermock.integration.rule;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Heater;
import org.mockito.Mock;

/**
 * Resources for the {@link PartialSharedResorcesTest}
 */
public class PartialSharedResources {

  @Mock Heater mHeater;

  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;

}
