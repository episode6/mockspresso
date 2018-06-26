package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.extend.testext.BuildTestMockspresso;
import com.episode6.hackit.mockspresso.extend.testext.TestMockspresso;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class AbstractMockspressoExtensionTest {

  static final TypeToken<Provider<String>> TYPE_TOKEN = new TypeToken<Provider<String>>() {};
  static final DependencyKey<Provider<String>> DEP_KEY = DependencyKey.of(TYPE_TOKEN);
  static final Provider<String> STRING_PROVIDER = new Provider<String>() {
    @Override
    public String get() {
      return "provided_string";
    }
  };
  static final ObjectProvider<String> STRING_OBJ_PROVIDER = new ObjectProvider<String>() {
    @Override
    public String get() throws Throwable {
      return "object_provided_string";
    }
  };
  static final ObjectProvider<Provider<String>> STRING_PROVIDER_OBJECT_PROVIDER = new ObjectProvider<Provider<String>>() {
    @Override
    public Provider<String> get() throws Throwable {
      return STRING_PROVIDER;
    }
  };

  @Rule public final MockitoRule mockito = MockitoJUnit.rule();

  @Mock Mockspresso mockspresso;
  @Mock Mockspresso.Rule mockspressoRule;
  @Mock Mockspresso.Builder mockspressoBuilder;

  @Mock Statement base;
  @Mock FrameworkMethod method;
  @Mock Object target;
  @Mock MockspressoPlugin plugin;
  @Mock TestRule testRule;
  @Mock MethodRule methodRule;
  @Mock MockerConfig mockerConfig;
  @Mock InjectionConfig injectionConfig;
  @Mock SpecialObjectMaker specialObjectMaker;
  List<SpecialObjectMaker> specialObjectMakers = Collections.singletonList(specialObjectMaker);


  @Test
  public void testMockspressoExtension() {
    TestMockspresso testMockspresso = BuildTestMockspresso.wrapMockspresso(mockspresso);

    testMockspresso.create(String.class);
    verify(mockspresso).create(String.class);

    testMockspresso.create(TYPE_TOKEN);
    verify(mockspresso).create(TYPE_TOKEN);

    testMockspresso.inject(this);
    verify(mockspresso).inject(this);

    TestMockspresso.Builder newBuilder = testMockspresso.buildUpon();
    verify(mockspresso).buildUpon();
    assertThat(newBuilder).isNotNull();
  }

  @Test
  public void testMockspressoExtensionRule() {
    TestMockspresso.Rule testMockspressoRule = BuildTestMockspresso.wrapRule(mockspressoRule);

    testMockspressoRule.create(String.class);
    verify(mockspressoRule).create(String.class);

    testMockspressoRule.create(TYPE_TOKEN);
    verify(mockspressoRule).create(TYPE_TOKEN);

    testMockspressoRule.inject(this);
    verify(mockspressoRule).inject(this);

    testMockspressoRule.apply(base, method, target);
    verify(mockspressoRule).apply(base, method, target);

    TestMockspresso.Builder newBuilder = testMockspressoRule.buildUpon();
    verify(mockspressoRule).buildUpon();
    assertThat(newBuilder).isNotNull();
  }

  @Test
  public void testMockspressoExtensionBuilder() {
    TestMockspresso.Builder testMockspressoBuilder = BuildTestMockspresso.wrapBuilder(mockspressoBuilder);

    testMockspressoBuilder.plugin(plugin);
    verify(mockspressoBuilder).plugin(plugin);

    testMockspressoBuilder.outerRule(testRule);
    verify(mockspressoBuilder).outerRule(testRule);

    testMockspressoBuilder.outerRule(methodRule);
    verify(mockspressoBuilder).outerRule(methodRule);

    testMockspressoBuilder.innerRule(testRule);
    verify(mockspressoBuilder).innerRule(testRule);

    testMockspressoBuilder.innerRule(methodRule);
    verify(mockspressoBuilder).innerRule(methodRule);

    testMockspressoBuilder.testResources(target);
    verify(mockspressoBuilder).testResources(target);

    testMockspressoBuilder.testResourcesWithoutLifecycle(target);
    verify(mockspressoBuilder).testResourcesWithoutLifecycle(target);

    testMockspressoBuilder.mocker(mockerConfig);
    verify(mockspressoBuilder).mocker(mockerConfig);

    testMockspressoBuilder.injector(injectionConfig);
    verify(mockspressoBuilder).injector(injectionConfig);

    testMockspressoBuilder.specialObjectMaker(specialObjectMaker);
    verify(mockspressoBuilder).specialObjectMaker(specialObjectMaker);

    testMockspressoBuilder.specialObjectMakers(specialObjectMakers);
    verify(mockspressoBuilder).specialObjectMakers(specialObjectMakers);

    testMockspressoBuilder.dependency(String.class, "hello");
    verify(mockspressoBuilder).dependency(String.class, "hello");

    testMockspressoBuilder.dependency(TYPE_TOKEN, STRING_PROVIDER);
    verify(mockspressoBuilder).dependency(TYPE_TOKEN, STRING_PROVIDER);

    testMockspressoBuilder.dependency(DEP_KEY, STRING_PROVIDER);
    verify(mockspressoBuilder).dependency(DEP_KEY, STRING_PROVIDER);

    testMockspressoBuilder.dependencyProvider(String.class, STRING_OBJ_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(String.class, STRING_OBJ_PROVIDER);

    testMockspressoBuilder.dependencyProvider(TYPE_TOKEN, STRING_PROVIDER_OBJECT_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(TYPE_TOKEN, STRING_PROVIDER_OBJECT_PROVIDER);

    testMockspressoBuilder.dependencyProvider(DEP_KEY, STRING_PROVIDER_OBJECT_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(DEP_KEY, STRING_PROVIDER_OBJECT_PROVIDER);

    testMockspressoBuilder.realObject(String.class);
    verify(mockspressoBuilder).realObject(String.class);

    testMockspressoBuilder.realObject(TYPE_TOKEN);
    verify(mockspressoBuilder).realObject(TYPE_TOKEN);

    testMockspressoBuilder.realObject(DEP_KEY);
    verify(mockspressoBuilder).realObject(DEP_KEY);

    testMockspressoBuilder.realObject(DependencyKey.of(String.class), String.class);
    verify(mockspressoBuilder).realObject(DependencyKey.of(String.class), String.class);

    testMockspressoBuilder.realObject(DEP_KEY, TYPE_TOKEN);
    verify(mockspressoBuilder).realObject(DEP_KEY, TYPE_TOKEN);

    TestMockspresso newMockspresso = testMockspressoBuilder.build();
    verify(mockspressoBuilder).build();
    assertThat(newMockspresso).isNotNull();

    TestMockspresso.Rule newMockspressoRule = testMockspressoBuilder.buildRule();
    verify(mockspressoBuilder).buildRule();
    assertThat(newMockspressoRule).isNotNull();
  }
}
