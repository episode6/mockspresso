package com.episode6.hackit.mockspresso.dagger;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin;

/**
 * A simple dagger plugin that builds off of the javax injector
 * and adds special object support for {@link dagger.Lazy}
 */
public class DaggerMockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert dagger.Lazy.class != null;
    return builder
        .plugin(new JavaxInjectMockspressoPlugin())
        .specialObjectMaker(new DaggerLazyMaker());

  }
}
