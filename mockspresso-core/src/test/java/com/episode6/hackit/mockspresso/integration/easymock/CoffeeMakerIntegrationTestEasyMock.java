package com.episode6.hackit.mockspresso.integration.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.easymock.EasyMockPlugin;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.*;
import com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mockspresso integration test of an integration test
 * Tests {@link CoffeeMakers} using real objects instead of mocks
 *
 * Technically this test doesn't even touch the MockerConfig, our two  versions are
 * just a sanity check.
 */
@RunWith(JUnit4.class)
public class CoffeeMakerIntegrationTestEasyMock {

  @Rule public final Mockspresso.Rule simpleMockspresso = Mockspresso.Builders.simple()
      .plugin(EasyMockPlugin.getInstance())
      .realObject(DependencyKey.of(Heater.class), CoffeeMakerComponents.RealHeater.class)
      .realObject(DependencyKey.of(Pump.class), CoffeeMakerComponents.RealWaterPump.class)
      .buildRule();

  private final Mockspresso injectionMockspresso = simpleMockspresso.buildUpon()
      .plugin(JavaxInjectMockspressoPlugin.getInstance())
      .build();

  @RealObject(implementation = CoffeeMakerComponents.RealCoffeeGrounds.class) CoffeeGrounds mCoffeeGrounds;

  @Test
  public void testSimpleCoffeeMaker() {
    CoffeeMakers.SimpleCoffeeMaker coffeeMaker = simpleMockspresso.create(CoffeeMakers.SimpleCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testConstructorInjectedCofferMaker() {
    CoffeeMakers.ConstructorInjectedCofferMaker coffeeMaker = injectionMockspresso
        .create(CoffeeMakers.ConstructorInjectedCofferMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testFieldInjectedCoffeeMakerWithGroundsProvider() {
    CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider coffeeMaker = injectionMockspresso
        .create(CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider.class);

    Coffee coffee = coffeeMaker.brew();

    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testMethodInjectedCoffeeMaker() {
    CoffeeMakers.MethodInjectedCoffeeMaker coffeeMaker = injectionMockspresso
        .create(CoffeeMakers.MethodInjectedCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testMixedInjectionCoffeeMaker() {
    CoffeeMakers.MixedInjectionCoffeeMaker coffeeMaker = injectionMockspresso.
        create(CoffeeMakers.MixedInjectionCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertCoffeeIsAsExpected(coffee);
  }

  private void assertCoffeeIsAsExpected(Coffee coffee) {
    assertThat(coffee.getWater())
        .isNotNull()
        .is(rawClass(CoffeeMakerComponents.RealWater.class));

    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(rawClass(CoffeeMakerComponents.RealCoffeeGrounds.class))
        .isEqualTo(mCoffeeGrounds);
  }
}
