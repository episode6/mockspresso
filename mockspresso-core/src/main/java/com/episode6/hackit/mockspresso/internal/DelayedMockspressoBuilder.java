package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.List;

/**
 * An implementation of {@link Mockspresso.Builder} that also acts as its own Mockspresso instance.
 * This allows us to buildUpon an "incomplete" @Rule, and as long as none of the 'create' methods are
 * called before the rule has a statement applied to it, everything should still work.
 */
class DelayedMockspressoBuilder extends AbstractDelayedMockspresso implements Mockspresso.Builder {

  private final MockspressoBuilderImpl mBuilder;
  private final BuiltInPluginPicker mBuiltInPluginPicker;

  DelayedMockspressoBuilder(Provider<MockspressoBuilderImpl> builderProvider) {
    super(builderProvider);
    mBuilder = builderProvider.get();
    mBuiltInPluginPicker = new BuiltInPluginPicker(this);
  }

  void setParent(@Nullable MockspressoConfigContainer parentConfig) {
    if (parentConfig == null) {
      setDelegate(null);
    } else {
      // I'm kind of concerned about this. We're basically hoping that
      // we never apply two completely different parents to this builder.
      // With our current usage patterns, it shouldn't be possible, since the only
      // place we call this is from our @Rule.
      MockspressoBuilderImpl builder = mBuilder.deepCopy();
      builder.setParent(parentConfig);
      setDelegate(builder.buildInternal());
    }
  }

  @Override
  public Builder plugin(MockspressoPlugin plugin) {
    mBuilder.plugin(plugin);
    return this;
  }

  @Override
  public Builder outerRule(TestRule testRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @Override
  public Builder outerRule(MethodRule methodRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @Override
  public Builder innerRule(TestRule testRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @Override
  public Builder innerRule(MethodRule methodRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @Override
  public Builder testResources(Object objectWithResources) {
    mBuilder.testResources(objectWithResources);
    return this;
  }

  @Override
  public Builder testResourcesWithoutLifecycle(Object objectWithResources) {
    mBuilder.testResourcesWithoutLifecycle(objectWithResources);
    return this;
  }

  @Override
  public Builder mocker(MockerConfig mockerConfig) {
    mBuilder.mocker(mockerConfig);
    return this;
  }

  @Override
  public Builder injector(InjectionConfig injectionConfig) {
    mBuilder.injector(injectionConfig);
    return this;
  }

  @Override
  public InjectorPicker injector() {
    return mBuiltInPluginPicker;
  }

  @Override
  public Builder specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
    mBuilder.specialObjectMaker(specialObjectMaker);
    return this;
  }

  @Override
  public Builder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
    mBuilder.specialObjectMakers(specialObjectMakers);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(Class<T> clazz, V value) {
    mBuilder.dependency(clazz, value);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(TypeToken<T> typeToken, V value) {
    mBuilder.dependency(typeToken, value);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(DependencyKey<T> key, V value) {
    mBuilder.dependency(key, value);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(Class<T> clazz, ObjectProvider<V> value) {
    mBuilder.dependency(clazz, value);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(TypeToken<T> typeToken, ObjectProvider<V> value) {
    mBuilder.dependency(typeToken, value);
    return this;
  }

  @Override
  public <T, V extends T> Builder dependency(DependencyKey<T> key, ObjectProvider<V> value) {
    mBuilder.dependency(key, value);
    return this;
  }

  @Override
  public <T> Builder realObject(Class<T> objectClass) {
    mBuilder.realObject(objectClass);
    return this;
  }

  @Override
  public <T> Builder realObject(TypeToken<T> objectToken) {
    mBuilder.realObject(objectToken);
    return this;
  }

  @Override
  public <T> Builder realObject(DependencyKey<T> keyAndImplementation) {
    mBuilder.realObject(keyAndImplementation);
    return this;
  }

  @Override
  public <T> Builder realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
    mBuilder.realObject(key, implementationClass);
    return this;
  }

  @Override
  public <T> Builder realObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
    mBuilder.realObject(key, implementationToken);
    return this;
  }

  @Override
  public Mockspresso build() {
    return this;
  }

  @Override
  public Rule buildRule() {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }
}
