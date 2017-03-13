package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.mockspresso.easymock.MockspressoEasyMockConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration test for Mockspresso usage with easymock
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoEasyMockIntegrationTest {

  @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.simple()
      .mockerConfig(new MockspressoEasyMockConfig())
      .buildRule();

  @Test
  public void placeholderTest() {
    Chop.i("EasyMock Rule Validated");
  }

}
