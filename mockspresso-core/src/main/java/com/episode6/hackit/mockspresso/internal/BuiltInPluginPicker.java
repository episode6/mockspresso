package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.exception.MissingDependencyError;

/**
 * Implementation of {@link Mockspresso.MockerPicker} and {@link Mockspresso.InjectorPicker} which enables
 * simple activation of "built-in" mockspresso plugins. For plugins with their own "optional" dependencies,
 * the BuiltInPluginPicker will catch any NoClassDefFoundErrors and wrap them with a {@link MissingDependencyError}
 * that better explains exactly which dependencies are missing.
 */
class BuiltInPluginPicker implements Mockspresso.MockerPicker, Mockspresso.InjectorPicker {

  private final Mockspresso.Builder mBuilder;

  BuiltInPluginPicker(Mockspresso.Builder builder) {
    mBuilder = builder;
  }

  @Override
  public Mockspresso.Builder mockito() {
    try {
      return mBuilder.plugin(com.episode6.hackit.mockspresso.mockito.MockitoPlugin.getInstance());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
    }
  }

  @Override
  public Mockspresso.Builder easyMock() {
    try {
      return mBuilder.plugin(com.episode6.hackit.mockspresso.easymock.EasyMockPlugin.getInstance());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("org.easymock:easymock v3.4", e);
    }
  }

  @Override
  public Mockspresso.Builder simple() {
    return mBuilder.plugin(com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin.getInstance());
  }

  @Override
  public Mockspresso.Builder javax() {
    return mBuilder.plugin(com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin.getInstance());
  }
}
