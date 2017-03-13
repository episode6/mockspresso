package com.episode6.hackit.mockspresso.plugin.simple;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * Encapsulates the simplest viable mockspresso components (excluding a mockerConfig).
 */
public class SimpleMockspressoPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injectionConfig(new SimpleInjectionConfig());
  }
}
