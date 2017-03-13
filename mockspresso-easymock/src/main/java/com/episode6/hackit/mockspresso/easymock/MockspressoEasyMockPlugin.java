package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * An implementation of MockspressoPlugin that applies the Easy mock mocker config
 */
public class MockspressoEasyMockPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.mockerConfig(new MockspressoEasyMockConfig());
  }
}
