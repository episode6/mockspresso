package com.episode6.hackit.mockspresso.mockito.integration;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGrounds;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGroundsFactory;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectBySimpleConfig;
import static com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition;
import static com.episode6.hackit.mockspresso.mockito.MockspressoMockitoPluginsJavaSupport.automaticFactories;
import static com.episode6.hackit.mockspresso.mockito.MockspressoMockitoPluginsJavaSupport.mockByMockito;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Quick integration test for the automatic factories plugin.
 */
@RunWith(JUnit4.class)
public class AutoFactoryTest {

  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
      .plugin(injectBySimpleConfig())
      .plugin(mockByMockito())
      .plugin(automaticFactories(CoffeeGroundsFactory.class))
      .buildRule();

  // this mock should be returned by the CoffeeGroundsFactory that gets injected into mCoffeeMaker
  @Mock CoffeeGrounds mCoffeeGrounds;

  @RealObject CoffeeMakers.GroundsFactoryCoffeeMaker mCoffeeMaker;

  @Test
  public void testCoffeeMaker() {
    Coffee coffee = mCoffeeMaker.brew();

    assertThat(coffee.getCoffeeGrounds())
        .isNotNull()
        .is(mockCondition())
        .isEqualTo(mCoffeeGrounds);
  }
}
