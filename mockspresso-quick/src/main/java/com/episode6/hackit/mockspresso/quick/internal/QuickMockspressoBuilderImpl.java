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
 *
 */
public class QuickMockspressoBuilderImpl implements QuickMockspressoBuilder {

  private final Mockspresso.Builder mDelegate;

  public QuickMockspressoBuilderImpl(Mockspresso.Builder delegate) {
    mDelegate = delegate;
  }

  @Override
  public QuickMockspressoBuilder.InjectorPicker injector() {
    return null;
  }

  @Override
  public QuickMockspressoBuilder.PluginPicker plugin() {
    return null;
  }

  @Override
  public QuickMockspressoBuilder.MockerPicker mocker() {
    return null;
  }



  @Override
  public QuickMockspressoBuilder plugin(MockspressoPlugin plugin) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder outerRule(TestRule testRule) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder outerRule(MethodRule methodRule) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder innerRule(TestRule testRule) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder innerRule(MethodRule methodRule) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder testResources(Object objectWithResources) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder testResourcesWithoutLifecycle(Object objectWithResources) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder mocker(MockerConfig mockerConfig) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder injector(InjectionConfig injectionConfig) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
    return null;
  }

  @Override
  public QuickMockspressoBuilder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
    return null;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(Class<T> clazz, V value) {
    return null;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(
      TypeToken<T> typeToken, V value) {
    return null;
  }

  @Override
  public <T, V extends T> QuickMockspressoBuilder dependency(
      DependencyKey<T> key, V value) {
    return null;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(Class<T> objectClass) {
    return null;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(TypeToken<T> objectToken) {
    return null;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(DependencyKey<T> keyAndImplementation) {
    return null;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
    return null;
  }

  @Override
  public <T> QuickMockspressoBuilder realObject(
      DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
    return null;
  }

  @Override
  public Mockspresso build() {
    return null;
  }

  @Override
  public Mockspresso.Rule buildRule() {
    return null;
  }
}
