package com.episode6.hackit.mockspresso.basic.plugin.simple;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * Encapsulates the simplest viable mockspresso components (excluding a mocker).
 */
public class SimpleInjectMockspressoPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injector(new SimpleInjectionConfig());
  }
}
