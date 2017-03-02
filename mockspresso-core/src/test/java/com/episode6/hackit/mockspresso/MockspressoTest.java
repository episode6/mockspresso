package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.mockspresso.internal.DependencyMap;
import com.episode6.hackit.mockspresso.internal.MockspressoRuleImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Main mockspresso test
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoTest {

  @Rule public final MockspressoRule mockspresso = MockspressoBuilder.fromTest(this).buildRule();

  /**
   * placeholder test
   */
  @Test
  public void placeholderTest() {
    DependencyMap objUnderTest = mockspresso.constructRealObject(DependencyMap.class);
    Chop.i("TEST SUCCESS");
  }
}
