package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * An implementation of MockspressoPlugin that applies the Easy mock mocker config
 */
public class MockspressoEasyMockPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final MockspressoEasyMockPlugin INSTANCE = new MockspressoEasyMockPlugin();
  public static MockspressoEasyMockPlugin getInstance() {
    return INSTANCE;
  }

  private MockspressoEasyMockPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.mockerConfig(MockspressoEasyMockConfig.getInstance());
  }
}
