package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A MockspressoPlugin that applies the mockito mocker config.
 */
public class MockitoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.mocker(new MockitoMockerConfig());
  }
}
