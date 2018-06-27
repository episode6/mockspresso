package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.extend.AbstractMockspressoExtension;
import com.episode6.hackit.mockspresso.quick.exception.MissingDependencyError;

/**
 *
 */
class QuickMockspressoImpl extends AbstractMockspressoExtension<QuickMockspresso.Builder> implements QuickMockspresso {

  private QuickMockspressoImpl(Mockspresso delegate) {
    super(delegate);
  }

  @Override
  public QuickMockspresso.Builder buildUpon() {
    return new QuickMockspressoImpl.Builder(getDelegate().buildUpon());
  }

  static class Rule extends AbstractMockspressoExtension.Rule<QuickMockspresso.Builder> implements QuickMockspresso.Rule {

    private Rule(Rule delegate) {
      super(delegate);
    }

    @Override
    public QuickMockspresso.Builder buildUpon() {
      return new QuickMockspressoImpl.Builder(getDelegate().buildUpon());
    }
  }

  static class Builder extends AbstractMockspressoExtension.Builder<
      QuickMockspresso,
      QuickMockspresso.Rule,
      QuickMockspresso.Builder> implements QuickMockspresso.Builder {

    private final PluginPickerImpl mPluginPicker;

    Builder(Mockspresso.Builder delegate) {
      super(delegate);
      mPluginPicker = new PluginPickerImpl(this);
    }

    @Override
    public QuickMockspresso build() {
      return new QuickMockspressoImpl(getDelegate().build());
    }

    @Override
    public QuickMockspresso.Rule buildRule() {
      return new QuickMockspressoImpl.Rule(getDelegate().buildRule());
    }

    @Override
    public InjectorPicker injector() {
      return mPluginPicker;
    }

    @Override
    public PluginPicker plugin() {
      return mPluginPicker;
    }

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

    @Override
    public QuickMockspresso.Builder simple() {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin());
    }

    @Override
    public QuickMockspresso.Builder javax() {
      return mBuilder.plugin(new com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin());
    }

    @Override
    public QuickMockspresso.Builder dagger() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.dagger.DaggerMockspressoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("com.google.dagger:dagger or com.squareup.dagger:dagger", e);
      }
    }

    @Override
    public QuickMockspresso.Builder mockito() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.mockito.MockitoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("org.mockito:mockito-core v2.x", e);
      }
    }

    @Override
    public QuickMockspresso.Builder easyMock() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.easymock.EasyMockPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("org.easymock:easymock v3.4", e);
      }
    }

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

    @Override
    public QuickMockspresso.Builder guava() {
      try {
        return mBuilder.plugin(new com.episode6.hackit.mockspresso.guava.GuavaMockspressoPlugin());
      } catch (NoClassDefFoundError e) {
        throw new MissingDependencyError("com.google.guava:guava", e);
      }
    }

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
