package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A MockspressoPlugin that applies the mockito mocker config.
 */
public class MockitoPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final MockitoPlugin INSTANCE = new MockitoPlugin();
  public static MockitoPlugin getInstance() {
    return INSTANCE;
  }

  private MockitoPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.mocker(MockitoMockerConfig.getInstance());
  }
}
