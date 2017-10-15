package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;

/**
 * Implementation of  {@link Mockspresso.InjectorPicker} which enables
 * simple activation of "built-in" mockspresso plugins.
 */
class BuiltInPluginPicker implements Mockspresso.InjectorPicker {

  private final Mockspresso.Builder mBuilder;

  BuiltInPluginPicker(Mockspresso.Builder builder) {
    mBuilder = builder;
  }

  @Override
  public Mockspresso.Builder simple() {
    return mBuilder.plugin(new com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin());
  }

  @Override
  public Mockspresso.Builder javax() {
    return mBuilder.plugin(new com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin());
  }
}
