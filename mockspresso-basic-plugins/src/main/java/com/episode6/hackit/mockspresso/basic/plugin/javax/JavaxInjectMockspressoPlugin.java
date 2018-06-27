package com.episode6.hackit.mockspresso.basic.plugin.javax;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A Mockspresso Plugin that applies logic to simulate a javax.inject compatible DI framework.
 */
public class JavaxInjectMockspressoPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injector(new JavaxInjectionConfig())
        .specialObjectMaker(new ProviderMaker());
  }
}