package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Heater;
import org.mockito.Mock;

/**
 * Resources for the {@link PartialSharedResorcesTest}
 */
public class PartialSharedResources {

  @Mock Heater mHeater;

  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;

}
