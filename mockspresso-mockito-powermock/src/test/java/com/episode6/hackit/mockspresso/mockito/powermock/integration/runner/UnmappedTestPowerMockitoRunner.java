package com.episode6.hackit.mockspresso.mockito.powermock.integration.runner;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.annotation.Unmapped;
import com.episode6.hackit.mockspresso.api.ObjectProvider;
import com.episode6.hackit.mockspresso.exception.RepeatedDependencyDefinedException;
import com.episode6.hackit.mockspresso.quick.QuickBuildMockspresso;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.Coffee;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeGrounds;
import com.episode6.hackit.mockspresso.testing.testobjects.coffee.CoffeeMakers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.inject.Provider;

import static com.episode6.hackit.mockspresso.mockito.powermock.Conditions.mockCondition;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Ensures that we can mock multiple mocks of the same type / key if we
 * use the @Unmapped annotation.
 */
@PrepareForTest(UnmappedTestPowerMockitoRunner.TestClass.class)
@RunWith(PowerMockRunner.class)
public class UnmappedTestPowerMockitoRunner {

  @Rule public final Mockspresso.Rule mockspresso = QuickBuildMockspresso.with()
      .injector().javax()
      .mocker().mockitoWithPowerMock()
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

  @Test(expected = RepeatedDependencyDefinedException.class)
  public void testNotUnmappedFailureMixedDefineMethod3() {
    mockspresso.buildUpon()
        .testResources(new OneRealClass())
        .dependencyProvider(TestClass.class, new ObjectProvider<TestClass>() {
          @Override
          public TestClass get() {
            return new TestClass();
          }
        })
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

  static final class TestClass {}
}
