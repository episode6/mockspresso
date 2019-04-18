package com.episode6.hackit.mockspresso.dagger;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A simple dagger plugin that builds off of the javax injector
 * and adds special object support for {@link dagger.Lazy}
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `injectByDaggerConfig()` and its
 * JavaSupport counterpart {@link MockspressoDaggerPluginsJavaSupport#injectByDaggerConfig()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class DaggerMockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert dagger.Lazy.class != null;
    return builder
        .plugin(new com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin())
        .specialObjectMaker(new DaggerLazyMaker());

  }
}
