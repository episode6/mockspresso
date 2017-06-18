package com.episode6.hackit.mockspresso.dagger2;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A simple dagger2 plugin that builds off of the javax injector
 * and adds special object support for {@link dagger.Lazy}
 */
public class Dagger2MockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert dagger.Lazy.class != null;
    return builder
        .injector().javax()
        .specialObjectMaker(new Dagger2LazyMaker());

  }
}
