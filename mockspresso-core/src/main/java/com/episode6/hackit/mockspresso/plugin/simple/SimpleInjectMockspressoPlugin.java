package com.episode6.hackit.mockspresso.plugin.simple;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * Encapsulates the simplest viable mockspresso components (excluding a mockerConfig).
 */
public class SimpleInjectMockspressoPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final SimpleInjectMockspressoPlugin INSTANCE = new SimpleInjectMockspressoPlugin();
  public static SimpleInjectMockspressoPlugin getInstance() {
    return INSTANCE;
  }

  private SimpleInjectMockspressoPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injectionConfig(SimpleInjectionConfig.getInstance());
  }
}
