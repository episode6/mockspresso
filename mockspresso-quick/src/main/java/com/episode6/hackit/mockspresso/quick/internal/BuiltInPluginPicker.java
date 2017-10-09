package com.episode6.hackit.mockspresso.quick.internal;

import com.episode6.hackit.mockspresso.exception.MissingDependencyError;
import com.episode6.hackit.mockspresso.quick.QuickMockspressoBuilder;

/**
 * Implementation of {@link QuickMockspressoBuilder.MockerPicker} and {@link QuickMockspressoBuilder.PluginPicker} which enables
 * simple activation of "built-in" mockspresso plugins. For plugins with their own "optional" dependencies,
 * the BuiltInPluginPicker will catch any NoClassDefFoundErrors and wrap them with a {@link MissingDependencyError}
 * that better explains exactly which dependencies are missing.
 */
class BuiltInPluginPicker implements QuickMockspressoBuilder.MockerPicker, QuickMockspressoBuilder.InjectorPicker, QuickMockspressoBuilder.PluginPicker {

  private final QuickMockspressoBuilder mBuilder;

  BuiltInPluginPicker(QuickMockspressoBuilder builder) {
    mBuilder = builder;
  }

  @Override
  public QuickMockspressoBuilder mockito() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.MockitoPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
    }
  }

  @Override
  public QuickMockspressoBuilder easyMock() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.EasyMockPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("org.easymock:easymock v3.4", e);
    }
  }

  @Override
  public QuickMockspressoBuilder mockitoWithPowerMock() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError(
          "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2 or powermock-module-junit4 v1.7.0",
          e);
    }
  }

  @Override
  public QuickMockspressoBuilder mockitoWithPowerMockRule() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoRulePlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError(
          "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
          e);
    }
  }

  @Override
  public QuickMockspressoBuilder easyMockWithPowerMock() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError(
          "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock or powermock-module-junit4 v1.7.0",
          e);
    }
  }

  @Override
  public QuickMockspressoBuilder easyMockWithPowerMockRule() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockRulePlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError(
          "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
          e);
    }
  }

  @Override
  public QuickMockspressoBuilder simple() {
    return mBuilder.plugin(new com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin());
  }

  @Override
  public QuickMockspressoBuilder javax() {
    return mBuilder.plugin(new com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin());
  }

  @Override
  public QuickMockspressoBuilder dagger() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.dagger.DaggerMockspressoPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("com.google.dagger:dagger or com.squareup.dagger:dagger", e);
    }
  }

  @Override
  public QuickMockspressoBuilder guava() {
    try {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.guava.GuavaMockspressoPlugin());
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("com.google.guava:guava", e);
    }
  }

  @Override
  public QuickMockspressoBuilder automaticFactories(Class<?>... factoryClasses) {
    try {
      return mBuilder.specialObjectMaker(com.episode6.hackit.mockspresso.mockito.MockitoAutoFactoryMaker.create(factoryClasses));
    } catch (NoClassDefFoundError e) {
      throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
    }
  }
}
