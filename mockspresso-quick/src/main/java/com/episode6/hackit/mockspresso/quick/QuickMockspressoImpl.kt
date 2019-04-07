package com.episode6.hackit.mockspresso.quick

import com.episode6.hackit.mockspresso.Mockspresso
import com.episode6.hackit.mockspresso.extend.AbstractMockspressoExtension
import com.episode6.hackit.mockspresso.quick.exception.MissingDependencyError

/**
 * Internal implementation for quick mockspresso
 */
@Deprecated(QUICK_DEPRECATION_MESSAGE)
internal class QuickMockspressoImpl(delegate: Mockspresso) : QuickMockspresso,
    AbstractMockspressoExtension<QuickMockspresso.Builder>(
        delegate,
        ::Builder) {

  @Deprecated(QUICK_DEPRECATION_MESSAGE)
  internal class Rule(delegate: Mockspresso.Rule) : QuickMockspresso.Rule,
      AbstractMockspressoExtension.Rule<QuickMockspresso.Builder>(
          delegate,
          ::Builder)

  @Deprecated(QUICK_DEPRECATION_MESSAGE)
  internal class Builder(delegate: Mockspresso.Builder) : QuickMockspresso.Builder,
      AbstractMockspressoExtension.Builder<QuickMockspresso, QuickMockspresso.Rule, QuickMockspresso.Builder>(
          delegate,
          ::QuickMockspressoImpl,
          ::Rule) {

    private val pluginPicker = PluginPickerImpl(this)
    
    override fun injector(): QuickMockspresso.InjectorPicker = pluginPicker
    override fun plugin(): QuickMockspresso.PluginPicker = pluginPicker
    override fun mocker(): QuickMockspresso.MockerPicker = pluginPicker
  }

  @Deprecated(QUICK_DEPRECATION_MESSAGE)
  internal class PluginPickerImpl(private val builder: QuickMockspresso.Builder) :
      QuickMockspresso.InjectorPicker, QuickMockspresso.MockerPicker, QuickMockspresso.PluginPicker {

    private fun tryApply(errorMessage: String, instruction: ()->QuickMockspresso.Builder): QuickMockspresso.Builder {
      try {
        return instruction()
      } catch (e: NoClassDefFoundError) {
        throw MissingDependencyError(errorMessage, e)
      }
    }

    override fun simple(): QuickMockspresso.Builder =
        builder.plugin(com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin())
    
    override fun javax(): QuickMockspresso.Builder =
        builder.plugin(com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin())

    override fun dagger(): QuickMockspresso.Builder = tryApply("com.google.dagger:dagger or com.squareup.dagger:dagger") {
      builder.plugin(com.episode6.hackit.mockspresso.dagger.DaggerMockspressoPlugin())
    }

    override fun mockito(): QuickMockspresso.Builder = tryApply("org.mockito:mockito-core v2.x") {
      builder.plugin(com.episode6.hackit.mockspresso.mockito.MockitoPlugin())
    }

    override fun easyMock(): QuickMockspresso.Builder = tryApply("org.easymock:easymock v3.4") {
      builder.plugin(com.episode6.hackit.mockspresso.easymock.EasyMockPlugin())
    }

    override fun mockitoWithPowerMock(): QuickMockspresso.Builder = tryApply("org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2 or powermock-module-junit4 v1.7.0") {
      builder.plugin(com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoPlugin())
    }

    override fun mockitoWithPowerMockRule(): QuickMockspresso.Builder = tryApply("org.mockito:mockito-core v2.x, org.powermock:powermock-api-mockito2, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0") {
      builder.plugin(com.episode6.hackit.mockspresso.mockito.powermock.PowerMockitoRulePlugin())
    }

    override fun easyMockWithPowerMock(): QuickMockspresso.Builder = tryApply("org.easymock:easymock v3.4, org.powermock:powermock-api-easymock or powermock-module-junit4 v1.7.0") {
      builder.plugin(com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockPlugin())
    }

    override fun easyMockWithPowerMockRule(): QuickMockspresso.Builder = tryApply("org.easymock:easymock v3.4, org.powermock:powermock-api-easymock, powermock-module-junit4 or powermock-module-junit4-rule v1.7.0") {
      builder.plugin(com.episode6.hackit.mockspresso.easymock.powermock.EasyPowerMockRulePlugin())
    }

    override fun guava(): QuickMockspresso.Builder = tryApply("com.google.guava:guava") {
      builder.plugin(com.episode6.hackit.mockspresso.guava.GuavaMockspressoPlugin())
    }

    override fun automaticFactories(vararg factoryClasses: Class<*>): QuickMockspresso.Builder = tryApply("org.mockito:mockito-core v2.x") {
      builder.specialObjectMaker(com.episode6.hackit.mockspresso.mockito.MockitoAutoFactoryMaker.create(*factoryClasses))
    }
  }
}
