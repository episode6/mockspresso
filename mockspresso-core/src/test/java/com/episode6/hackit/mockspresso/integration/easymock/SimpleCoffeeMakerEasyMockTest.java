package com.episode6.hackit.mockspresso.integration.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.easymock.EasyMockPlugin;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.*;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.Conditions.easyMockMock;
import static org.easymock.EasyMock.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mockspresso integration test / example
 * Tests {@link CoffeeMakers.SimpleCoffeeMaker} with EasyMock
 */
@RunWith(JUnit4.class)
public class SimpleCoffeeMakerEasyMockTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.simple()
      .plugin(EasyMockPlugin.getInstance())
      .buildRule();

  @Mock Water mWater;
  @Mock Heater mHeater;
  @Mock Pump mPump;

  @RealObject CoffeeMakers.SimpleCoffeeMaker mCoffeeMaker;

  @Test
  public void testBrew() {
    expect(mPump.pump()).andReturn(mWater);
    mHeater.heat(mWater);
    expectLastCall().once();
    replay(mPump, mHeater);

    Coffee coffee = mCoffeeMaker.brew();

    verify(mPump, mHeater);
    assertThat(coffee).isNot(easyMockMock());
    assertThat(coffee.getWater()).isEqualTo(mWater);
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(easyMockMock());
  }

}
