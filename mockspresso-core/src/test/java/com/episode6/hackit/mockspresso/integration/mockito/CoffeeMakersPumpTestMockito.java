package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Pump;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Water;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mockspresso integration test
 * Tests {@link CoffeeMakers} with Mockito
 *
 * In this test, we only mock the {@link Pump} (and the {@link Water} it returns, and allow
 * everything else to be auto-mocked. If automocking were broken tests would fail with NPEs.
 */
@RunWith(JUnit4.class)
public class CoffeeMakersPumpTestMockito {

  @Rule public final Mockspresso.Rule injectionMockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .buildRule();

  private final Mockspresso simpleMockspresso = injectionMockspresso.buildUpon()
      .plugin(SimpleInjectMockspressoPlugin.getInstance())
      .build();

  @Mock Pump mPump;
  @Mock Water mWater;

  // Because the @Rule we're using is based on javaxInjection, we can define all 4 injected
  // coffee makers here and test those.
  @RealObject CoffeeMakers.ConstructorInjectedCofferMaker mConstructorInjectedCofferMaker;
  @RealObject CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider mFieldInjectedCoffeeMakerWithGroundsProvider;
  @RealObject CoffeeMakers.MethodInjectedCoffeeMaker mMethodInjectedCoffeeMaker;
  @RealObject CoffeeMakers.MixedInjectionCoffeeMaker mMixedInjectionCoffeeMaker;

  @Before
  public void setup() {
    when(mPump.pump()).thenReturn(mWater);
  }

  @Test
  public void testConstructorInjectedCoffeeMaker() {
    assertThat(mConstructorInjectedCofferMaker)
        .is(rawClass(CoffeeMakers.ConstructorInjectedCofferMaker.class));

    Coffee coffee = mConstructorInjectedCofferMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  @Test
  public void testFieldInjectedCoffeeMakerWithGroundsProvider() {
    assertThat(mFieldInjectedCoffeeMakerWithGroundsProvider)
        .is(rawClass(CoffeeMakers.FieldInjectedCoffeeMakerWithGroundsProvider.class));

    Coffee coffee = mFieldInjectedCoffeeMakerWithGroundsProvider.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  @Test
  public void testMethodInjectedCoffeeMaker() {
    assertThat(mMethodInjectedCoffeeMaker)
        .is(rawClass(CoffeeMakers.MethodInjectedCoffeeMaker.class));

    Coffee coffee = mMethodInjectedCoffeeMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  @Test
  public void testMixedInjectionCoffeeMaker() {
    assertThat(mMixedInjectionCoffeeMaker)
        .is(rawClass(CoffeeMakers.MixedInjectionCoffeeMaker.class));

    Coffee coffee = mMixedInjectionCoffeeMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  @Test
  public void testSimpleCoffeeMaker() {
    CoffeeMakers.SimpleCoffeeMaker simpleCoffeeMaker = simpleMockspresso.create(CoffeeMakers.SimpleCoffeeMaker.class);
    assertThat(simpleCoffeeMaker).is(rawClass(CoffeeMakers.SimpleCoffeeMaker.class));

    Coffee coffee = simpleCoffeeMaker.brew();

    verifyInterationsAndCoffeeObject(coffee);
  }

  private void verifyInterationsAndCoffeeObject(Coffee coffee) {
    // verify our defined pump mock was called;
    verify(mPump).pump();

    // assert that coffee was actually created contains the Water object we expect
    assertThat(coffee)
        .isNotNull()
        .isNot(mockitoMock());
    assertThat(coffee.getWater())
        .isNotNull()
        .isEqualTo(mWater);

    // coffee grounds should be a mock even though we never defined one.
    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(mockitoMock());
  }
}
