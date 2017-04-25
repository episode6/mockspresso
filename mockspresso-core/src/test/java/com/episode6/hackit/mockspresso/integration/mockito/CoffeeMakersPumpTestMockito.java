package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Water;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mockspresso integration test
 * Tests {@link CoffeeMakers} with Mockito
 *
 * In this test, we only mock the {@link Pump} (and the {@link Water} it returns, and allow
 * everything else to be auto-mocked. If automocking were broken tests would fail with NPEs.
 *
 * We also demonstrate the use of resource sharing via {@link #t}
 */
@RunWith(JUnit4.class)
public class CoffeeMakersPumpTestMockito {

  private final PumpTestResources t = new PumpTestResources();
  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .initializerWithFields(t)
      .buildRule();

  @Test
  public void testConstructorInjectedCoffeeMaker() {
    assertThat(t.mConstructorInjectedCofferMaker)
        .is(rawClass(CoffeeMakers.ConstructorInjectedCofferMaker.class));

    Coffee coffee = t.mConstructorInjectedCofferMaker.brew();

    t.verifyInteractionsAndCoffeeObject(coffee);
  }

  @Test
  public void testFieldInjectedCoffeeMakerWithGroundsProvider() {
    assertThat(t.mFieldInjectedCoffeeMakerWithGroundsProvider)
        .is(rawClass(CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider.class));

    Coffee coffee = t.mFieldInjectedCoffeeMakerWithGroundsProvider.brew();

    t.verifyInteractionsAndCoffeeObject(coffee);
  }

  @Test
  public void testMethodInjectedCoffeeMaker() {
    assertThat(t.mMethodInjectedCoffeeMaker)
        .is(rawClass(CoffeeMakers.MethodInjectedCoffeeMaker.class));

    Coffee coffee = t.mMethodInjectedCoffeeMaker.brew();

    t.verifyInteractionsAndCoffeeObject(coffee);
  }

  @Test
  public void testMixedInjectionCoffeeMaker() {
    assertThat(t.mMixedInjectionCoffeeMaker)
        .is(rawClass(CoffeeMakers.MixedInjectionCoffeeMaker.class));

    Coffee coffee = t.mMixedInjectionCoffeeMaker.brew();

    t.verifyInteractionsAndCoffeeObject(coffee);
  }

  @Test
  public void testSimpleCoffeeMaker() {
    assertThat(t.mSimpleCoffeeMaker).is(rawClass(CoffeeMakers.SimpleCoffeeMaker.class));

    Coffee coffee = t.mSimpleCoffeeMaker.brew();

    t.verifyInteractionsAndCoffeeObject(coffee);
  }
}
