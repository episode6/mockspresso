package com.episode6.hackit.mockspresso.basic.plugin.simple;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * Encapsulates the simplest viable mockspresso components (excluding a mocker).
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `injectBySimpleConfig()` and its
 * JavaSupport counterpart
 * {@link com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport#injectBySimpleConfig()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class SimpleInjectMockspressoPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.injector(new SimpleInjectionConfig());
  }
}
