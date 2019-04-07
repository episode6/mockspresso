package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.extend.MockspressoExtension;
import org.jetbrains.annotations.NotNull;

/**
 * A mockspresso extension for bootstrapping / general use
 *
 * @deprecated see DEPRECATED.kt
 */
@Deprecated
public interface QuickMockspresso extends MockspressoExtension<QuickMockspresso.Builder> {

  /**
   * @deprecated see DEPRECATED.kt
   */
  @Deprecated
  interface Rule extends MockspressoExtension.Rule<QuickMockspresso.Builder> { }

  /**
   * @deprecated see DEPRECATED.kt
   */
  @Deprecated
  interface Builder extends MockspressoExtension.Builder<
      QuickMockspresso,
      QuickMockspresso.Rule,
      QuickMockspresso.Builder> {

    /**
     * Apply one of the built-in {@link InjectionConfig}s to this builder.
     * @return A {@link InjectorPicker} that will apply an injectionConfig to this builder.
     */
    @NotNull InjectorPicker injector();

    /**
     * Apply one of the built-in {@link MockspressoPlugin}s to this builder.
     * @return A {@link PluginPicker} that will apply a plugin to this builder.
     */
    @NotNull PluginPicker plugin();

    /**
     * Apply one of the built-in {@link MockerConfig}s to this builder.
     * @return A {@link MockerPicker} that will apply a mockerConfig to this builder.
     */
    @NotNull MockerPicker mocker();

  }

  /**
   * A selector for one of the built in injection configs
   * @deprecated see DEPRECATED.kt
   */
  @Deprecated
  interface InjectorPicker {

    /**
     * Applies the {@link InjectionConfig} for simple creation of objects via
     * their shortest constructor.
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder simple();

    /**
     * Applies the {@link InjectionConfig} for javax.inject based object creation
     * (looks for constructors, fields and methods annotated with @Inject).
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder javax();

    /**
     * Applies an {@link InjectionConfig} for dagger. This is the same as the
     * {@link #javax()} injector with additional support for dagger's Lazy interface.
     * Requires your project have a dependency on 'com.google.dagger:dagger' or
     * 'com.squareup.dagger:dagger'
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder dagger();
  }

  /**
   * A selector for one of the built-in mocker configs
   * @deprecated see DEPRECATED.kt
   */
  @Deprecated
  interface MockerPicker {

    /**
     * Applies the {@link MockerConfig} for Mockito.
     * Requires your project have a dependency on 'org.mockito:mockito-core' v2.x
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder mockito();

    /**
     * Applies the {@link MockerConfig} for EasyMock.
     * Requires your project have a dependency on 'org.easymock:easymock' v3.4
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder easyMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito.
     * Requires your project have a dependency on org.mockito:mockito-core v2.x,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder mockitoWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #mockitoWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder mockitoWithPowerMockRule();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock.
     * Requires your project have a dependency on org.easymock:easymock v3.4,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     * <p>
     * WARNING, the @org.easymock.Mock annotation may not work correctly when using Mockspresso +
     * easymock + PowerMockRunner, as easymock overwrites Mockspresso's annotated mocks at the last minute.
     * To work around this problem, use powermock's @Mock, @MockNice and @MockStrict annotations instead.
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder easyMockWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #easyMockWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder easyMockWithPowerMockRule();
  }


  /**
   * A selector for one of the built in plugins
   * @deprecated see DEPRECATED.kt
   */
  @Deprecated
  interface PluginPicker {

    /**
     * Applies a {@link MockspressoPlugin} thats adds special object handling
     * for some of the types provided by guava.
     * * Requires your project have a dependency on 'com.google.guava:guava'
     *
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder guava();

    /**
     * Applies special object handling for the provided factory classes. The
     * applicable objects will be mocked by mockito, but with a default answer
     * that returns objects from mockspresso's dependency map (similar to how
     * the javax() injector automatically binds Providers, but applied to any
     * factory class, including generics).
     * <p>
     * Requires your project have a dependency on 'org.mockito:mockito-core' v2.x but
     * does NOT require using mockito as your mocker.
     *
     * @param factoryClasses The factory classes that should be auto-mocked and bound
     *                       if they are encountered in your test.
     * @return The {@link QuickMockspresso.Builder} for your mockspresso instance
     */
    @NotNull QuickMockspresso.Builder automaticFactories(Class<?>... factoryClasses);
  }

}
