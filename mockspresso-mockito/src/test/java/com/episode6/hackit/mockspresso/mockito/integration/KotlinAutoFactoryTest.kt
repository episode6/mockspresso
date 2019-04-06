package com.episode6.hackit.mockspresso.mockito.integration

import com.episode6.hackit.mockspresso.BuildMockspresso
import com.episode6.hackit.mockspresso.annotation.Dependency
import com.episode6.hackit.mockspresso.annotation.RealObject
import com.episode6.hackit.mockspresso.basic.plugin.injectBySimpleConfig
import com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition
import com.episode6.hackit.mockspresso.mockito.automaticFactories
import com.episode6.hackit.mockspresso.mockito.mocksByMockito
import com.episode6.hackit.mockspresso.testing.matches
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGrounds
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGroundsFactory
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers
import com.nhaarman.mockitokotlin2.mock
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * Quick integration test for the automatic factories plugin.
 */
class KotlinAutoFactoryTest {

  @get:Rule
  val mockspresso = BuildMockspresso.with()
      .injectBySimpleConfig()
      .mocksByMockito()
      .automaticFactories(CoffeeGroundsFactory::class)
      .buildRule()

  // this mock should be returned by the CoffeeGroundsFactory that gets injected into mCoffeeMaker
  @Dependency
  val coffeeGrounds: CoffeeGrounds = mock()

  @RealObject
  lateinit var coffeeMaker: CoffeeMakers.GroundsFactoryCoffeeMaker

  @Test
  fun testCoffeeMaker() {
    val coffee = coffeeMaker.brew()

    assertThat(coffee.coffeeGrounds)
        .isNotNull
        .matches(mockCondition())
        .isEqualTo(coffeeGrounds)
  }
}
