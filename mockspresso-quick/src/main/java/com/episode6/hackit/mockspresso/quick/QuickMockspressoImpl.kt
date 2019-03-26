package com.episode6.hackit.mockspresso.quick

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.extend.AbstractMockspressoExtension
import com.episode6.hackit.mockspresso.quick.exception.MissingDependencyError

/**
 * Internal implementation for quick mockspresso
 */
internal class QuickMockspressoImpl(delegate: Mockspresso) : QuickMockspresso,
    AbstractMockspressoExtension<QuickMockspresso.Builder>(
        delegate,
        ::Builder) {

  internal class Rule(delegate: Mockspresso.Rule) : QuickMockspresso.Rule,
      AbstractMockspressoExtension.Rule<QuickMockspresso.Builder>(
          delegate,
          ::Builder)

  internal class Builder(delegate: Mockspresso.Builder) : QuickMockspresso.Builder,
      AbstractMockspressoExtension.Builder<QuickMockspresso, QuickMockspresso.Rule, QuickMockspresso.Builder>(
          delegate,
          ::QuickMockspressoImpl,
          ::Rule) {

    private val pluginPicker: PluginPickerImpl

    init {
      pluginPicker = PluginPickerImpl(this)
    }

    override fun injector(): QuickMockspresso.InjectorPicker {
      return pluginPicker
    }

    override fun plugin(): QuickMockspresso.PluginPicker {
      return pluginPicker
    }

    override fun mocker(): QuickMockspresso.MockerPicker {
      return pluginPicker
    }
  }

  internal class PluginPickerImpl(private val builder: QuickMockspresso.Builder) : QuickMockspresso.InjectorPicker,
      QuickMockspresso.MockerPicker, QuickMockspresso.PluginPicker {

    override fun simple(): QuickMockspresso.Builder {
      return builder.plugin(com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin())
    }

    override fun javax(): QuickMockspresso.Builder {
      return builder.plugin(com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin())
    }

    override fun dagger(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.dagger.DaggerMockspressoPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError("com.google.dagger:dagger or com.squareup.dagger:dagger", e)
      }

    }

    override fun mockito(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.mockito.MockitoPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError("org.mockito:mockito-core v2.x", e)
      }

    }

    override fun easyMock(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.easymock.EasyMockPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError("org.easymock:easymock v3.4", e)
      }

    }

    override fun mockitoWithPowerMock(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError(
            "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2 or powermock-module-junit4 v1.7.0",
            e)
      }

    }

    override fun mockitoWithPowerMockRule(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoRulePlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError(
            "org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
            e)
      }

    }

    override fun easyMockWithPowerMock(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError(
            "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock or powermock-module-junit4 v1.7.0",
            e)
      }

    }

    override fun easyMockWithPowerMockRule(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockRulePlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError(
            "org.easymock:easymock v3.4, org.powermock:powermock-api-easymock, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0",
            e)
      }

    }

    override fun guava(): QuickMockspresso.Builder {
      try {
        return builder.plugin(com.episode6.hackit.mockspresso.guava.GuavaMockspressoPlugin())
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError("com.google.guava:guava", e)
      }

    }

    override fun automaticFactories(vararg factoryClasses: Class<*>): QuickMockspresso.Builder {
      try {
        return builder.specialObjectMaker(com.episode6.hackit.mockspresso.mockito.MockitoAutoFactoryMaker.create(*factoryClasses))
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError("org.mockito:mockito-core v2.x", e)
      }

    }
  }
}
