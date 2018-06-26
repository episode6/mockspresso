package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.List;

/**
 * Extend these 3 interfaces to build your own Mockspresso api
 */
public interface MockspressoExtension<BLDR extends MockspressoExtension.Builder> extends Mockspresso {

  @Override
  BLDR buildUpon();

  interface Rule<BLDR extends MockspressoExtension.Builder> extends Mockspresso.Rule {

    @Override
    BLDR buildUpon();
  }

  interface Builder<
      EXT extends MockspressoExtension,
      RULE extends MockspressoExtension.Rule,
      BLDR extends MockspressoExtension.Builder> extends Mockspresso.Builder {

    @Override
    BLDR plugin(MockspressoPlugin plugin);

    @Override
    BLDR outerRule(TestRule testRule);

    @Override
    BLDR outerRule(MethodRule methodRule);

    @Override
    BLDR innerRule(TestRule testRule);

    @Override
    BLDR innerRule(MethodRule methodRule);

    @Override
    BLDR testResources(Object objectWithResources);

    @Override
    BLDR testResourcesWithoutLifecycle(Object objectWithResources);

    @Override
    BLDR mocker(MockerConfig mockerConfig);

    @Override
    BLDR injector(InjectionConfig injectionConfig);

    @Override
    BLDR specialObjectMaker(SpecialObjectMaker specialObjectMaker);

    @Override
    BLDR specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers);

    @Override
    <T, V extends T> BLDR dependency(Class<T> clazz, V value);

    @Override
    <T, V extends T> BLDR dependency(
        TypeToken<T> typeToken, V value);

    @Override
    <T, V extends T> BLDR dependency(
        DependencyKey<T> key, V value);

    @Override
    <T, V extends T> BLDR dependencyProvider(
        Class<T> clazz, ObjectProvider<V> value);

    @Override
    <T, V extends T> BLDR dependencyProvider(
        TypeToken<T> typeToken, ObjectProvider<V> value);

    @Override
    <T, V extends T> BLDR dependencyProvider(
        DependencyKey<T> key, ObjectProvider<V> value);

    @Override
    <T> BLDR realObject(Class<T> objectClass);

    @Override
    <T> BLDR realObject(TypeToken<T> objectToken);

    @Override
    <T> BLDR realObject(DependencyKey<T> keyAndImplementation);

    @Override
    <T> BLDR realObject(
        DependencyKey<T> key, Class<? extends T> implementationClass);

    @Override
    <T> BLDR realObject(
        DependencyKey<T> key, TypeToken<? extends T> implementationToken);

    @Override
    EXT build();

    @Override
    RULE buildRule();
  }
}
