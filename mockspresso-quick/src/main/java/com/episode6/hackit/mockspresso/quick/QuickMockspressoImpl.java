package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.extend.AbstractMockspressoExtension;
import com.episode6.hackit.mockspresso.quick.exception.MissingDependencyError;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
class QuickMockspressoImpl extends AbstractMockspressoExtension<QuickMockspresso.Builder> implements QuickMockspresso {

  private QuickMockspressoImpl(@NotNull Mockspresso delegate) {
    super(delegate, Builder::new);
  }

  static class Rule extends AbstractMockspressoExtension.Rule<QuickMockspresso.Builder> implements QuickMockspresso.Rule {

    private Rule(@NotNull Rule delegate) {
      super(delegate, QuickMockspressoImpl.Builder::new);
    }
  }

  static class Builder extends AbstractMockspressoExtension.Builder<
      QuickMockspresso,
      QuickMockspresso.Rule,
      QuickMockspresso.Builder> implements QuickMockspresso.Builder {

    private final PluginPickerImpl mPluginPicker;

    Builder(@NotNull Mockspresso.Builder delegate) {
      super(delegate, QuickMockspressoImpl::new, Rule::new);
      mPluginPicker = new PluginPickerImpl(this);
    }

    @NotNull
    @Override
    public InjectorPicker injector() {
      return mPluginPicker;
    }

    @NotNull
    @Override
    public PluginPicker plugin() {
      return mPluginPicker;
    }

    @NotNull
    @Override
    public MockerPicker mocker() {
      return mPluginPicker;
    }
  }

  static class PluginPickerImpl implements InjectorPicker, MockerPicker, PluginPicker {

    private final QuickMockspresso.Builder mBuilder;

    PluginPickerImpl(QuickMockspresso.Builder builder) {
      mBuilder = builder;
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder simple() {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin());
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder javax() {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin());
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder dagger() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.dagger.DaggerMockspressoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("com.google.dagger:dagger or com.squareup.dagger:dagger", e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder mockito() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.MockitoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder easyMock() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.EasyMockPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("org.easymock:easymock v3.4", e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder mockitoWithPowerMock() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError(
            "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2 or powermock-module-junit4 v1.7.0",
            e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder mockitoWithPowerMockRule() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoRulePlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError(
            "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
            e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder easyMockWithPowerMock() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError(
            "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock or powermock-module-junit4 v1.7.0",
            e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder easyMockWithPowerMockRule() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockRulePlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError(
            "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
            e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder guava() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.guava.GuavaMockspressoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("com.google.guava:guava", e);
      }
    }

    @NotNull
    @Override
    public QuickMockspresso.Builder automaticFactories(Class<?>... factoryClasses) {
      try {
        return mBuilder.specialObjectMaker(com.episode6.hackit.mockspresso.mockito.MockitoAutoFactoryMaker.create(factoryClasses));
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
      }
    }
  }
}
