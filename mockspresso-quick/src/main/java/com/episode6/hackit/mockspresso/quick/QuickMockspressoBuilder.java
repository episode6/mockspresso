package com.episode6.hackit.mockspresso.quick;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.List;

/**
 *
 */
public interface QuickMockspressoBuilder extends Mockspresso.Builder {

  /**
   * Apply one of the built-in {@link InjectionConfig}s to this builder.
   * @return A {@link InjectorPicker} that will apply an injectionConfig to this builder.
   */
  QuickMockspressoBuilder.InjectorPicker injector();

  /**
   * Apply one of the built-in {@link MockspressoPlugin}s to this builder.
   * @return A {@link PluginPicker} that will apply a plugin to this builder.
   */
  QuickMockspressoBuilder.PluginPicker plugin();

  /**
   * Apply one of the built-in {@link MockerConfig}s to this builder.
   * @return A {@link MockerPicker} that will apply a mockerConfig to this builder.
   */
  QuickMockspressoBuilder.MockerPicker mocker();

  @Override
  QuickMockspressoBuilder plugin(MockspressoPlugin plugin);

  @Override
  QuickMockspressoBuilder outerRule(TestRule testRule);

  @Override
  QuickMockspressoBuilder outerRule(MethodRule methodRule);

  @Override
  QuickMockspressoBuilder innerRule(TestRule testRule);

  @Override
  QuickMockspressoBuilder innerRule(MethodRule methodRule);

  @Override
  QuickMockspressoBuilder testResources(Object objectWithResources);

  @Override
  QuickMockspressoBuilder testResourcesWithoutLifecycle(Object objectWithResources);

  @Override
  QuickMockspressoBuilder mocker(MockerConfig mockerConfig);

  @Override
  QuickMockspressoBuilder injector(InjectionConfig injectionConfig);

  @Override
  QuickMockspressoBuilder specialObjectMaker(SpecialObjectMaker specialObjectMaker);

  @Override
  QuickMockspressoBuilder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(Class<T> clazz, V value);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(
      TypeToken<T> typeToken, V value);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(
      DependencyKey<T> key, V value);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(Class<T> clazz, ObjectProvider<V> value);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(TypeToken<T> typeToken, ObjectProvider<V> value);

  @Override
  <T, V extends T> QuickMockspressoBuilder dependency(DependencyKey<T> key, ObjectProvider<V> value);

  @Override
  <T> QuickMockspressoBuilder realObject(Class<T> objectClass);

  @Override
  <T> QuickMockspressoBuilder realObject(TypeToken<T> objectToken);

  @Override
  <T> QuickMockspressoBuilder realObject(DependencyKey<T> keyAndImplementation);

  @Override
  <T> QuickMockspressoBuilder realObject(
      DependencyKey<T> key, Class<? extends T> implementationClass);

  @Override
  <T> QuickMockspressoBuilder realObject(
      DependencyKey<T> key, TypeToken<? extends T> implementationToken);

  /**
   * A selector for one of the built in injection configs
   */
  interface InjectorPicker extends Mockspresso.InjectorPicker {

    /**
     * {@inheritDoc}
     */
    @Override
    QuickMockspressoBuilder simple();

    /**
     * {@inheritDoc}
     */
    @Override
    QuickMockspressoBuilder javax();

    /**
     * Applies an {@link InjectionConfig} for dagger. This is the same as the
     * {@link #javax()} injector with additional support for dagger's Lazy interface.
     * Requires your project have a dependency on 'com.google.dagger:dagger' or
     * 'com.squareup.dagger:dagger'
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder dagger();
  }

  /**
   * A selector for one of the built-in mocker configs
   */
  interface MockerPicker {

    /**
     * Applies the {@link MockerConfig} for Mockito.
     * Requires your project have a dependency on 'org.mockito:mockito-core' v2.x
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder mockito();

    /**
     * Applies the {@link MockerConfig} for EasyMock.
     * Requires your project have a dependency on 'org.easymock:easymock' v3.4
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder easyMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito.
     * Requires your project have a dependency on org.mockito:mockito-core v2.x,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder mockitoWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #mockitoWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder mockitoWithPowerMockRule();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock.
     * Requires your project have a dependency on org.easymock:easymock v3.4,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     *
     * WARNING, the @org.easymock.Mock annotation may not work correctly when using Mockspresso +
     * easymock + PowerMockRunner, as easymock overwrites Mockspresso's annotated mocks at the last minute.
     * To work around this problem, use powermock's @Mock, @MockNice and @MockStrict annotations instead.
     *
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder easyMockWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #easyMockWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder easyMockWithPowerMockRule();
  }



  /**
   * A selector for one of the built in plugins
   */
  interface PluginPicker {

    /**
     * Applies a {@link MockspressoPlugin} thats adds special object handling
     * for some of the types provided by guava.
     * * Requires your project have a dependency on 'com.google.guava:guava'
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder guava();

    /**
     * Applies special object handling for the provided factory classes. The
     * applicable objects will be mocked by mockito, but with a default answer
     * that returns objects from mockspresso's dependency map (similar to how
     * the javax() injector automatically binds Providers, but applied to any
     * factory class, including generics).
     *
     * Requires your project have a dependency on 'org.mockito:mockito-core' v2.x but
     * does NOT require using mockito as your mocker.
     *
     * @param factoryClasses The factory classes that should be auto-mocked and bound
     *                       if they are encountered in your test.
     * @return The {@link QuickMockspressoBuilder} for your mockspresso instance
     */
    QuickMockspressoBuilder automaticFactories(Class<?>... factoryClasses);
  }
}
