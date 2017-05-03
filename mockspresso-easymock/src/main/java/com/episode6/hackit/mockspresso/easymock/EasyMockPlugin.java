package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * An implementation of MockspressoPlugin that applies the Easy mock mocker config
 */
public class EasyMockPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final EasyMockPlugin INSTANCE = new EasyMockPlugin();
  public static EasyMockPlugin getInstance() {
    return INSTANCE;
  }

  private EasyMockPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.mocker(EasyMockMockerConfig.getInstance());
  }
}
