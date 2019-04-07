package com.episode6.hackit.mockspresso.easymock.integration;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Water;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectByJavaxConfig;
import static com.episode6.hackit.mockspresso.easymock.MockspressoEasyMockPluginsJavaSupport.mockByEasyMock;
import static org.easymock.EasyMock.*;


/**
 * Tests that mockspresso shared resources are combined with resources from the test itself
 */
@RunWith(JUnit4.class)
public class PartialSharedResorcesTestEasyMock {

  private final PartialSharedResources t = new PartialSharedResources();
  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
      .plugin(injectByJavaxConfig())
      .plugin(mockByEasyMock())
      .testResources(t)
      .buildRule();

  @Mock Pump mPump;
  @Mock Water mWater;

  @RealObject CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider mFieldInjectedCoffeeMakerWithGroundsProvider;

  @Before
  public void setup() {
    expect(mPump.pump()).andReturn(mWater);
    replay(mPump);
  }

  @Test
  public void testSharedRealObjectsContainOurMocks() {
    t.mConstructorInjectedCofferMaker.brew();

    verify(mPump);
  }

  @Test
  public void testOurRealObjectsContainSharedMocks() {
    mFieldInjectedCoffeeMakerWithGroundsProvider.brew();

    verify(t.mHeater);
  }
}
