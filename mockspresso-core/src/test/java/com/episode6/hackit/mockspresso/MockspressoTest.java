package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.mockspresso.internal.DependencyMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Main mockspresso test
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.fromTest(this).buildRule();

  /**
   * placeholder test
   */
  @Test
  public void placeholderTest() {
    DependencyMap objUnderTest = mockspresso.create(DependencyMap.class);
    Chop.i("TEST SUCCESS");
  }
}
