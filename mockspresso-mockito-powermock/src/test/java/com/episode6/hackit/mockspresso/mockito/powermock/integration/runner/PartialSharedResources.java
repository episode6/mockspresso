package com.episode6.hackit.mockspresso.mockito.powermock.integration.runner;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Heater;
import org.mockito.Mock;

/**
 * Resources for the {@link PartialSharedResorcesTestPowerMockitoRunner}
 */
public class PartialSharedResources {

  @Mock Heater mHeater;

  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;

}
