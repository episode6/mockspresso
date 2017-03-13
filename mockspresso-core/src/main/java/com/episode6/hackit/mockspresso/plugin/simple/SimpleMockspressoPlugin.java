package com.episode6.hackit.mockspresso.plugin.simple;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * Encapsulates the simplest viable mockspresso components (excluding a mockerConfig).
 */
public class SimpleMockspressoPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final SimpleMockspressoPlugin INSTANCE = new SimpleMockspressoPlugin();
  public static SimpleMockspressoPlugin getInstance() {
    return INSTANCE;
  }

  private SimpleMockspressoPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injectionConfig(SimpleInjectionConfig.getInstance());
  }
}
