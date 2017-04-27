package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Water;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests that mockspresso shared resources are combined with resources from the test itself
 */
@RunWith(JUnit4.class)
public class PartialSharedResorcesTest {

  private final PartialSharedResources t = new PartialSharedResources();
  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .testResources(t)
      .buildRule();

  @Mock Pump mPump;
  @Mock Water mWater;

  @RealObject CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider mFieldInjectedCoffeeMakerWithGroundsProvider;

  @Before
  public void setup() {
    when(mPump.pump()).thenReturn(mWater);
  }

  @Test
  public void testSharedRealObjectsContainOurMocks() {
    t.mConstructorInjectedCofferMaker.brew();

    verify(mPump).pump();
  }

  @Test
  public void testOurRealObjectsContainSharedMocks() {
    mFieldInjectedCoffeeMakerWithGroundsProvider.brew();
    
    verify(t.mHeater).heat(mWater);
  }
}
