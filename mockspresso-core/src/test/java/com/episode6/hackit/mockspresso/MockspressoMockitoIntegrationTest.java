package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.mockspresso.mockito.MockspressoMockitoConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration test for Mockspresso usage with mockito
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoMockitoIntegrationTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.simple()
      .mockerConfig(new MockspressoMockitoConfig())
      .buildRule();

  @Test
  public void placeholderTest() {
    Chop.i("Mockito Rule Validated");
  }
}
