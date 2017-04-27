package com.episode6.hackit.mockspresso.integration.agnostic;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeGrounds;
import com.episode6.hackit.mockspresso.integration.testobjects.coffee.CoffeeMakers;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.inject.Provider;

import static com.episode6.hackit.mockspresso.Conditions.mockitoMock;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Ensures that we can mock multiple mocks of the same type / key if we
 * use the @Unmapped annotation.
 */
@RunWith(DefaultTestRunner.class)
public class UnmappedTest {

  @Rule public final Mockspresso.Rule invalidMockspresso = Mockspresso.Builders.javaxInjection()
      .plugin(MockitoPlugin.getInstance())
      .buildRule();

  @Mock Provider<CoffeeGrounds> mGroundsProvider;

  // Without @Unmapped these duplicate mocks would result in a RepeatedDependencyDefinedException
  @Unmapped @Mock CoffeeGrounds mFirstGrounds;
  @Unmapped @Mock CoffeeGrounds mSecondGrounds;

  @RealObject CoffeeMakers.MixedInjectionCoffeeMaker mCoffeeMaker;

  // This will also be a real object, injected with the same mocks as mCoffeeMaker
  // (when applicable), but it will be a different instance.
  @Unmapped @RealObject CoffeeMakers.MixedInjectionCoffeeMaker mCoffeeMaker2;

  @Test
  public void testGroundsNotReused() {
    when(mGroundsProvider.get()).thenReturn(mFirstGrounds).thenReturn(mSecondGrounds);

    Coffee firstCup = mCoffeeMaker.brew();
    Coffee secondCup = mCoffeeMaker.brew();

    assertThat(firstCup.getCoffeeGrounds()).isEqualTo(mFirstGrounds);
    assertThat(secondCup.getCoffeeGrounds()).isEqualTo(mSecondGrounds);
  }

  @Test
  public void testBothCoffeeMakersRealButDifferent() {
    assertThat(mCoffeeMaker)
        .isNotNull()
        .isNot(mockitoMock())
        .isNotEqualTo(mCoffeeMaker2);
    assertThat(mCoffeeMaker2)
        .isNotNull()
        .isNot(mockitoMock())
        .isNotEqualTo(mCoffeeMaker);
  }
}
