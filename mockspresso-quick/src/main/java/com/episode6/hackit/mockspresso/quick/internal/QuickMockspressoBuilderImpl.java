package com.episode6.hackit.mockspresso.quick.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.quick.QuickMockspressoBuilder;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.List;

/**
 * Implementation of QuickMockspressoBuilder
 */
public class QuickMockspressoBuilderImpl implements QuickMockspressoBuilder {

  private final Mockspresso.Builder mDelegate;

  public QuickMockspressoBuilderImpl(Mockspresso.Builder delegate) {
    mDelegate = delegate;
  }

  @Override
  public QuickMockspressoBuilder.InjectorPicker injector() {
    return new BuiltInPluginPicker(this);
  }

  @Override
  public QuickMockspressoBuilder.PluginPicker plugin() {
    return new BuiltInPluginPicker(this);
  }

  @Override
  public QuickMockspressoBuilder.MockerPicker mocker() {
    return new BuiltInPluginPicker(this);
  }
  
  
  @Override
  public QuickMockspressoBuilder plugin(MockspressoPlugin plugin) {
    mDelegate.plugin(plugin);
    return this;
  }

  @Override
  public QuickMockspressoBuilder outerRule(TestRule testRule) {
    mDelegate.outerRule(testRule);
    return this;
  }

  @Override
  public QuickMockspressoBuilder outerRule(MethodRule methodRule) {
    mDelegate.outerRule(methodRule);
    return this;
  }

  @Override
  public QuickMockspressoBuilder innerRule(TestRule testRule) {
    mDelegate.innerRule(testRule);
    return this;
  }

  @Override
  public QuickMockspressoBuilder innerRule(MethodRule methodRule) {
    mDelegate.innerRule(methodRule);
    return this;
  }

  @Override
  public QuickMockspressoBuilder testResources(Object objectWithResources) {
    mDelegate.testResources(objectWithResources);
    return this;
  }

  @Override
  public QuickMockspressoBuilder testResourcesWithoutLifecycle(Object objectWithResources) {
    mDelegate.testResourcesWithoutLifecycle(objectWithResources);
    return this;
  }

  @Override
  public QuickMockspressoBuilder mocker(MockerConfig mockerConfig) {
    mDelegate.mocker(mockerConfig);
    return this;
  }

  @Override
  public QuickMockspressoBuilder injector(InjectionConfig injectionConfig) {
    mDelegate.injector(injectionConfig);
    return this;
  }

  @Override
  public QuickMockspressoBuilder specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
    mDelegate.specialObjectMaker(specialObjectMaker);
    return this;
  }

  @Override
  public QuickMockspressoBuilder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
    mDelegate.specialObjectMakers(specialObjectMakers);
    return this;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(Class<T> clazz, V value) {
    mDelegate.dependency(clazz, value);
    return this;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(
      TypeToken<T> typeToken, V value) {
    mDelegate.dependency(typeToken, value);
    return this;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(
      DependencyKey<T> key, V value) {
    mDelegate.dependency(key, value);
    return this;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(Class<T> objectClass) {
    mDelegate.realObject(objectClass);
    return this;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(TypeToken<T> objectToken) {
    mDelegate.realObject(objectToken);
    return this;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(DependencyKey<T> keyAndImplementation) {
    mDelegate.realObject(keyAndImplementation);
    return this;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
    mDelegate.realObject(key, implementationClass);
    return this;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(
      DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
    mDelegate.realObject(key, implementationToken);
    return this;
  }

  @Override
  public Mockspresso build() {
    return mDelegate.build();
  }

  @Override
  public Mockspresso.Rule buildRule() {
    return mDelegate.buildRule();
  }
}
