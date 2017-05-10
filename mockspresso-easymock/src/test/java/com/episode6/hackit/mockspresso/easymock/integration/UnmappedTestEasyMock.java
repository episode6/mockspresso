package com.episode6.hackit.mockspresso.easymock.integration;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGrounds;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import org.easymock.Mock;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Provider;

import static com.episode6.hackit.mockspresso.easymock.Conditions.mockCondition;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Ensures that we can mock multiple mocks of the same type / key if we
 * use the @Unmapped annotation.
 */
public class UnmappedTestEasyMock {

  @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
      .injector().javax()
      .mocker().easyMock()
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
    expect(mGroundsProvider.get()).andReturn(mFirstGrounds).andReturn(mSecondGrounds);
    replay(mGroundsProvider);

    Coffee firstCup = mCoffeeMaker.brew();
    Coffee secondCup = mCoffeeMaker.brew();

    assertThat(firstCup.getCoffeeGrounds()).isEqualTo(mFirstGrounds);
    assertThat(secondCup.getCoffeeGrounds()).isEqualTo(mSecondGrounds);
  }

  @Test
  public void testBothCoffeeMakersRealButDifferent() {
    assertThat(mCoffeeMaker)
        .isNotNull()
        .isNot(mockCondition())
        .isNotEqualTo(mCoffeeMaker2);
    assertThat(mCoffeeMaker2)
        .isNotNull()
        .isNot(mockCondition())
        .isNotEqualTo(mCoffeeMaker);
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureMocksOnly() {
    mockspresso.buildUpon()
        .testResources(new MultipleMocksClass())
        .build();
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureRealObjectsOnly() {
    mockspresso.buildUpon()
        .testResources(new MultipleRealObjectsClass())
        .build();
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureMixed() {
    mockspresso.buildUpon()
        .testResources(new MultipleMixedClass())
        .build();
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureMixedDefineMethod1() {
    mockspresso.buildUpon()
        .testResources(new OneMockClass())
        .realObject(TestClass.class)
        .build();
  }

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureMixedDefineMethod2() {
    mockspresso.buildUpon()
        .testResources(new OneRealClass())
        .dependency(TestClass.class, new TestClass())
        .build();
  }

  private static class MultipleMocksClass {
    @Mock TestClass mTestClass1;
    @Mock TestClass mTestClass2;
  }

  private static class MultipleRealObjectsClass {
    @RealObject TestClass mTestClass1;
    @RealObject TestClass mTestClass2;
  }

  private static class MultipleMixedClass {
    @RealObject TestClass mTestClass1;
    @Mock TestClass mTestClass2;
  }

  private static class OneMockClass {
    @Mock TestClass mTestClass1;
  }

  private static class OneRealClass {
    @RealObject TestClass mTestClass1;
  }

  private static class TestClass {}
}
