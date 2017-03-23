package com.episode6.hackit.mockspresso.plugin.javax;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A Mockspresso Plugin that applies logic to simulate a javax.inject compatible DI framework.
 */
public class JavaxInjectMockspressoPlugin implements MockspressoPlugin {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final JavaxInjectMockspressoPlugin INSTANCE = new JavaxInjectMockspressoPlugin();
  public static JavaxInjectMockspressoPlugin getInstance() {
    return INSTANCE;
  }

  private JavaxInjectMockspressoPlugin() {}

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injectionConfig(JavaxInjectionConfig.getInstance())
        .specialObjectMaker(ProviderMaker.getInstance());
  }
}
