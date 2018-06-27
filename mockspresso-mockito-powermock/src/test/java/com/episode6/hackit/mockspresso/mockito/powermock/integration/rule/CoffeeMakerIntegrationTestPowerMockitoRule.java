package com.episode6.hackit.mockspresso.mockito.powermock.integration.rule;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.quick.QuickBuildMockspresso;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Named;

import static com.episode6.hackit.mockspresso.testing.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Mockspresso integration test of an integration test
 * Tests {@link CoffeeMakers} using real objects instead of mocks
 *
 * Technically this test doesn't even touch the MockerConfig, our two  versions are
 * just a sanity check.
 */
@RunWith(JUnit4.class)
public class CoffeeMakerIntegrationTestPowerMockitoRule {

  @Rule public final Mockspresso.Rule simpleMockspresso = QuickBuildMockspresso.with()
      .injector().simple()
      .mocker().mockitoWithPowerMockRule()
      .realObject(DependencyKey.of(Heater.class), CoffeeMakerComponents.RealHeater.class)
      .realObject(DependencyKey.of(Pump.class), CoffeeMakerComponents.RealWaterPump.class)
      .buildRule();

  private final Mockspresso injectionMockspresso = simpleMockspresso.buildUpon()
      .plugin(new JavaxInjectMockspressoPlugin())
      .build();

  @RealObject @Named("heater_name") final String heaterName = "NamedHeaterExample";
  @RealObject @Named("heater_number") final int heaterNumber = 12;
  @RealObject @Named("heater_numbers") final int[] heaterNumbers = new int[] {13, 17, 22};

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

  @Test
  public void testNamedHeaterRealObjectMapping() {
    CoffeeMakers.SimpleCoffeeMaker coffeeMaker = simpleMockspresso.buildUpon()
        .realObject(DependencyKey.of(Heater.class), CoffeeMakerComponents.NamedHeater.class)
        .build()
        .create(CoffeeMakers.SimpleCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertThat(coffeeMaker.getHeater())
        .is(rawClass(CoffeeMakerComponents.NamedHeater.class));
    assertThat(((CoffeeMakerComponents.NamedHeater)coffeeMaker.getHeater()).getName())
        .isEqualTo("NamedHeaterExample");
    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testNumberedHeaterRealObjectMapping() {
    CoffeeMakers.SimpleCoffeeMaker coffeeMaker = simpleMockspresso.buildUpon()
        .realObject(DependencyKey.of(Heater.class), CoffeeMakerComponents.NumberedHeater.class)
        .build()
        .create(CoffeeMakers.SimpleCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertThat(coffeeMaker.getHeater())
        .is(rawClass(CoffeeMakerComponents.NumberedHeater.class));
    assertThat(((CoffeeMakerComponents.NumberedHeater)coffeeMaker.getHeater()).getNumber())
        .isEqualTo(12);
    assertCoffeeIsAsExpected(coffee);
  }

  @Test
  public void testNumberedArrayHeaterRealObjectMapping() {
    CoffeeMakers.SimpleCoffeeMaker coffeeMaker = simpleMockspresso.buildUpon()
        .realObject(DependencyKey.of(Heater.class), CoffeeMakerComponents.NumberedArrayHeater.class)
        .build()
        .create(CoffeeMakers.SimpleCoffeeMaker.class);

    Coffee coffee = coffeeMaker.brew();

    assertThat(coffeeMaker.getHeater())
        .is(rawClass(CoffeeMakerComponents.NumberedArrayHeater.class));
    assertThat(((CoffeeMakerComponents.NumberedArrayHeater)coffeeMaker.getHeater()).getNumbers())
        .containsOnly(13, 17, 22);
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
