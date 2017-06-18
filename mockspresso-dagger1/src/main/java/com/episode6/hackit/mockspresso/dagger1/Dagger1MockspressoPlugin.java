package com.episode6.hackit.mockspresso.dagger1;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;

/**
 * A simple dagger1 plugin that builds off of the javax injector
 * and adds special object support for {@link dagger.Lazy}
 */
public class Dagger1MockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert dagger.Lazy.class != null;
    return builder
        .injector().javax()
        .specialObjectMaker(new Dagger1LazyMaker());

  }
}
