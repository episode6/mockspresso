package com.episode6.hackit.mockspresso.easymock.powermock.integration.rule;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.quick.QuickBuildMockspresso;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.*;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers.*;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.easymock.powermock.Conditions.mockCondition;
import static com.episode6.hackit.mockspresso.testing.Conditions.rawClass;
import static org.easymock.EasyMock.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mockspresso integration test
 * Tests {@link CoffeeMakers} with EasyMock
 *
 * In this test, we mock all dependencies of the target classes we're testing.
 *
 * In the first test, we test the @RealObject that is created by the test's @Rule.
 * In the rest of the tests we build upon the @Rule so we may create different types
 * of DI-compatible objects
 */
@RunWith(JUnit4.class)
public class CoffeeMakersMockEverythingTestEasyPowerMockRule {

  @Rule public final Mockspresso.Rule simpleMockspresso = QuickBuildMockspresso.with()
      .injector().simple()
      .mocker().easyMockWithPowerMockRule()
      .buildRule();

  private final Mockspresso injectionMockspresso = simpleMockspresso.buildUpon()
      .injector().javax()
      .build();

  @Mock Water mWater;
  @Mock Heater mHeater;
  @Mock Pump mPump;
  @Mock CoffeeGrounds mCoffeeGrounds;

  @RealObject SimpleCoffeeMaker mCoffeeMaker;

  @Before
  public void setup() {
    expect(mPump.pump()).andReturn(mWater);
    mHeater.heat(mWater);
    expectLastCall().once();
    replay(mPump, mHeater);
  }

  /**
   * Test a simple object with a normal constructor using, using the
   * {@link SimpleInjectMockspressoPlugin}
   */
  @Test
  public void testSimpleCoffeeMaker() {
    assertThat(mCoffeeMaker).is(rawClass(SimpleCoffeeMaker.class));

    Coffee coffee = mCoffeeMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  /**
   * Test an object that is created by javax.inject rules, using the
   * {@link JavaxInjectMockspressoPlugin}
   */
  @Test
  public void testConstructorInjectedCoffeeMaker() {
    ConstructorInjectedCofferMaker cofferMaker = injectionMockspresso
        .create(ConstructorInjectedCofferMaker.class);
    assertThat(cofferMaker).is(rawClass(ConstructorInjectedCofferMaker.class));

    Coffee coffee = cofferMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  /**
   * Test an object that is created by javax.inject rules, using the
   * {@link JavaxInjectMockspressoPlugin}
   */
  @Test
  public void testFieldInjedCoffeeMaker() {
    FieldInjectedCoffeeMakerWithGroundsProvider cofferMaker = injectionMockspresso
        .create(FieldInjectedCoffeeMakerWithGroundsProvider.class);
    assertThat(cofferMaker).is(rawClass(FieldInjectedCoffeeMakerWithGroundsProvider.class));

    Coffee coffee = cofferMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  /**
   * Test an object that is created by javax.inject rules, using the
   * {@link JavaxInjectMockspressoPlugin}
   */
  @Test
  public void testMethodInjectedCoffeeMaker() {
    MethodInjectedCoffeeMaker cofferMaker = injectionMockspresso
        .create(MethodInjectedCoffeeMaker.class);
    assertThat(cofferMaker).is(rawClass(MethodInjectedCoffeeMaker.class));

    Coffee coffee = cofferMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  /**
   * Test an object that is created by javax.inject rules, using the
   * {@link JavaxInjectMockspressoPlugin}
   */
  @Test
  public void testMixedInjectionCoffeeMaker() {
    MixedInjectionCoffeeMaker cofferMaker = injectionMockspresso
        .create(MixedInjectionCoffeeMaker.class);
    assertThat(cofferMaker).is(rawClass(MixedInjectionCoffeeMaker.class));

    Coffee coffee = cofferMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  private void verifyInterationsAndCoffeeObject(Coffee coffee) {
    // verify our defined pump and heater mocks were called (as definded in setup())
    verify(mPump, mHeater);

    // assert that coffee was actually created contains the Water
    // and CoffeeGrounds objects we expect
    assertThat(coffee)
        .isNotNull()
        .isNot(mockCondition());
    assertThat(coffee.getWater())
        .isNotNull()
        .isEqualTo(mWater);
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .isEqualTo(mCoffeeGrounds);
  }
}
