package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.extend.testext.BuildTestMockspresso;
import com.episode6.hackit.mockspresso.extend.testext.TestMockspresso;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
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

  TestMockspresso testMockspresso;
  TestMockspresso.Rule testMockspressoRule;
  TestMockspresso.Builder testMockspressoBuilder;

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

  @Before
  public void setup() {
    testMockspresso = BuildTestMockspresso.wrapMockspresso(mockspresso);
    testMockspressoRule = BuildTestMockspresso.wrapRule(mockspressoRule);
    testMockspressoBuilder = BuildTestMockspresso.wrapBuilder(mockspressoBuilder);
  }

  @Test
  public void testMockspressoExtension_create1() {
    testMockspresso.create(String.class);
    verify(mockspresso).create(String.class);
  }

  @Test
  public void testMockspressoExtension_create2() {
    testMockspresso.create(TYPE_TOKEN);
    verify(mockspresso).create(TYPE_TOKEN);
  }

  @Test
  public void testMockspressoExtension_inject() {
    testMockspresso.inject(this);
    verify(mockspresso).inject(this);
  }

  @Test
  public void testMockspressoExtension_getDependency() {
    testMockspresso.getDependency(DEP_KEY);
    verify(mockspresso).getDependency(DEP_KEY);
  }

  @Test
  public void testMockspressoExtension_buildUpon() {
    TestMockspresso.Builder newBuilder = testMockspresso.buildUpon();
    verify(mockspresso).buildUpon();
    assertThat(newBuilder).isNotNull();
  }

  @Test
  public void testMockspressoExtensionRule_create1() {
    testMockspressoRule.create(String.class);
    verify(mockspressoRule).create(String.class);
  }

  @Test
  public void testMockspressoExtensionRule_create2() {
    testMockspressoRule.create(TYPE_TOKEN);
    verify(mockspressoRule).create(TYPE_TOKEN);
  }

  @Test
  public void testMockspressoExtensionRule_inject() {
    testMockspressoRule.inject(this);
    verify(mockspressoRule).inject(this);
  }

  @Test
  public void testMockspressoExtensionRule_getDependency() {
    testMockspressoRule.getDependency(DEP_KEY);
    verify(mockspressoRule).getDependency(DEP_KEY);
  }

  @Test
  public void testMockspressoExtensionRule_apply() {
    testMockspressoRule.apply(base, method, target);
    verify(mockspressoRule).apply(base, method, target);
  }

  @Test
  public void testMockspressoExtensionRule_buildUpon() {
    TestMockspresso.Builder newBuilder = testMockspressoRule.buildUpon();
    verify(mockspressoRule).buildUpon();
    assertThat(newBuilder).isNotNull();
  }

  @Test
  public void testMockspressoExtensionBuilder_plugin() {
    testMockspressoBuilder.plugin(plugin);
    verify(mockspressoBuilder).plugin(plugin);
  }

  @Test
  public void testMockspressoExtensionBuilder_outerRule1() {
    testMockspressoBuilder.outerRule(testRule);
    verify(mockspressoBuilder).outerRule(testRule);
  }

  @Test
  public void testMockspressoExtensionBuilder_outerRule2() {
    testMockspressoBuilder.outerRule(methodRule);
    verify(mockspressoBuilder).outerRule(methodRule);
  }

  @Test
  public void testMockspressoExtensionBuilder_innerRule1() {
    testMockspressoBuilder.innerRule(testRule);
    verify(mockspressoBuilder).innerRule(testRule);
  }

  @Test
  public void testMockspressoExtensionBuilder_innerRule2() {
    testMockspressoBuilder.innerRule(methodRule);
    verify(mockspressoBuilder).innerRule(methodRule);
  }

  @Test
  public void testMockspressoExtensionBuilder_testResources() {
    testMockspressoBuilder.testResources(target);
    verify(mockspressoBuilder).testResources(target);
  }

  @Test
  public void testMockspressoExtensionBuilder_testResourcesWithoutLifecycle() {
    testMockspressoBuilder.testResourcesWithoutLifecycle(target);
    verify(mockspressoBuilder).testResourcesWithoutLifecycle(target);
  }

  @Test
  public void testMockspressoExtensionBuilder_mocker() {
    testMockspressoBuilder.mocker(mockerConfig);
    verify(mockspressoBuilder).mocker(mockerConfig);
  }

  @Test
  public void testMockspressoExtensionBuilder_injector() {
    testMockspressoBuilder.injector(injectionConfig);
    verify(mockspressoBuilder).injector(injectionConfig);
  }

  @Test
  public void testMockspressoExtensionBuilder_specialObjectMaker() {
    testMockspressoBuilder.specialObjectMaker(specialObjectMaker);
    verify(mockspressoBuilder).specialObjectMaker(specialObjectMaker);
  }

  @Test
  public void testMockspressoExtensionBuilder_specialObjectMakers() {
    testMockspressoBuilder.specialObjectMakers(specialObjectMakers);
    verify(mockspressoBuilder).specialObjectMakers(specialObjectMakers);
  }

  @Test
  public void testMockspressoExtensionBuilder_dep1() {
    testMockspressoBuilder.dependency(String.class, "hello");
    verify(mockspressoBuilder).dependency(String.class, "hello");
  }

  @Test
  public void testMockspressoExtensionBuilder_dep2() {
    testMockspressoBuilder.dependency(TYPE_TOKEN, STRING_PROVIDER);
    verify(mockspressoBuilder).dependency(TYPE_TOKEN, STRING_PROVIDER);
  }

  @Test
  public void testMockspressoExtensionBuilder_dep3() {
    testMockspressoBuilder.dependency(DEP_KEY, STRING_PROVIDER);
    verify(mockspressoBuilder).dependency(DEP_KEY, STRING_PROVIDER);
  }

  @Test
  public void testMockspressoExtensionBuilder_depProvider1() {
    testMockspressoBuilder.dependencyProvider(String.class, STRING_OBJ_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(String.class, STRING_OBJ_PROVIDER);
  }

  @Test
  public void testMockspressoExtensionBuilder_depProvider2() {
    testMockspressoBuilder.dependencyProvider(TYPE_TOKEN, STRING_PROVIDER_OBJECT_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(TYPE_TOKEN, STRING_PROVIDER_OBJECT_PROVIDER);
  }

  @Test
  public void testMockspressoExtensionBuilder_depProvider3() {
    testMockspressoBuilder.dependencyProvider(DEP_KEY, STRING_PROVIDER_OBJECT_PROVIDER);
    verify(mockspressoBuilder).dependencyProvider(DEP_KEY, STRING_PROVIDER_OBJECT_PROVIDER);
  }

  @Test
  public void testMockspressoExtensionBuilder_realObject1() {
    testMockspressoBuilder.realObject(String.class);
    verify(mockspressoBuilder).realObject(String.class);
  }

  @Test
  public void testMockspressoExtensionBuilder_realObject2() {
    testMockspressoBuilder.realObject(TYPE_TOKEN);
    verify(mockspressoBuilder).realObject(TYPE_TOKEN);
  }

  @Test
  public void testMockspressoExtensionBuilder_realObject3() {
    testMockspressoBuilder.realObject(DEP_KEY);
    verify(mockspressoBuilder).realObject(DEP_KEY);
  }

  @Test
  public void testMockspressoExtensionBuilder_realObject4() {
    testMockspressoBuilder.realObject(DependencyKey.of(String.class), String.class);
    verify(mockspressoBuilder).realObject(DependencyKey.of(String.class), String.class);
  }

  @Test
  public void testMockspressoExtensionBuilder_realObject5() {
    testMockspressoBuilder.realObject(DEP_KEY, TYPE_TOKEN);
    verify(mockspressoBuilder).realObject(DEP_KEY, TYPE_TOKEN);
  }

  @Test
  public void testMockspressoExtensionBuilder_build() {
    TestMockspresso newMockspresso = testMockspressoBuilder.build();
    verify(mockspressoBuilder).build();
    assertThat(newMockspresso).isNotNull();
  }

  @Test
  public void testMockspressoExtensionBuilder_buildRule() {
    TestMockspresso.Rule newMockspressoRule = testMockspressoBuilder.buildRule();
    verify(mockspressoBuilder).buildRule();
    assertThat(newMockspressoRule).isNotNull();
  }
}
