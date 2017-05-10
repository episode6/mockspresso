package com.episode6.hackit.mockspresso.easymock.powermock.integration.rule;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Heater;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Water;
import org.easymock.Mock;
import org.junit.Before;

import static org.easymock.EasyMock.*;

/**
 * Resources for the {@link PartialSharedResorcesTestEasyPowerMockRule}
 */
public class PartialSharedResources {

  @Mock Heater mHeater;

  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;

  @Before
  public void setup() {
    mHeater.heat(anyObject(Water.class));
    expectLastCall();
    replay(mHeater);
  }
}
