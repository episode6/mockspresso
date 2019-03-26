package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import org.jetbrains.annotations.Nullable;
import javax.inject.Provider;
import java.util.List;

/**
 * An implementation of {@link Mockspresso.Builder} that also acts as its own Mockspresso instance.
 * This allows us to buildUpon an "incomplete" @Rule, and as long as none of the 'create' methods are
 * called before the rule has a statement applied to it, everything should still work.
 */
class DelayedMockspressoBuilder extends AbstractDelayedMockspresso implements Mockspresso.Builder {

  private final MockspressoBuilderImpl mBuilder;

  DelayedMockspressoBuilder(Provider<MockspressoBuilderImpl> builderProvider) {
    super(builderProvider);
    mBuilder = builderProvider.get();
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

  @NotNull
  @Override
  public Builder plugin(@NotNull MockspressoPlugin plugin) {
    mBuilder.plugin(plugin);
    return this;
  }

  @NotNull
  @Override
  public Builder outerRule(@NotNull TestRule testRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @NotNull
  @Override
  public Builder outerRule(@NotNull MethodRule methodRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @NotNull
  @Override
  public Builder innerRule(@NotNull TestRule testRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @NotNull
  @Override
  public Builder innerRule(@NotNull MethodRule methodRule) {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }

  @NotNull
  @Override
  public Builder testResources(@NotNull Object objectWithResources) {
    mBuilder.testResources(objectWithResources);
    return this;
  }

  @NotNull
  @Override
  public Builder testResourcesWithoutLifecycle(@NotNull Object objectWithResources) {
    mBuilder.testResourcesWithoutLifecycle(objectWithResources);
    return this;
  }

  @NotNull
  @Override
  public Builder mocker(@NotNull MockerConfig mockerConfig) {
    mBuilder.mocker(mockerConfig);
    return this;
  }

  @NotNull
  @Override
  public Builder injector(@NotNull InjectionConfig injectionConfig) {
    mBuilder.injector(injectionConfig);
    return this;
  }

  @NotNull
  @Override
  public Builder specialObjectMaker(@NotNull SpecialObjectMaker specialObjectMaker) {
    mBuilder.specialObjectMaker(specialObjectMaker);
    return this;
  }

  @NotNull
  @Override
  public Builder specialObjectMakers(@NotNull List<SpecialObjectMaker> specialObjectMakers) {
    mBuilder.specialObjectMakers(specialObjectMakers);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependency(@NotNull Class<T> clazz, V value) {
    mBuilder.dependency(clazz, value);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependency(@NotNull TypeToken<T> typeToken, V value) {
    mBuilder.dependency(typeToken, value);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependency(@NotNull DependencyKey<T> key, V value) {
    mBuilder.dependency(key, value);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependencyProvider(@NotNull Class<T> clazz, @NotNull ObjectProvider<V> value) {
    mBuilder.dependencyProvider(clazz, value);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependencyProvider(@NotNull TypeToken<T> typeToken, @NotNull ObjectProvider<V> value) {
    mBuilder.dependencyProvider(typeToken, value);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Builder dependencyProvider(@NotNull DependencyKey<T> key, @NotNull ObjectProvider<V> value) {
    mBuilder.dependencyProvider(key, value);
    return this;
  }

  @NotNull
  @Override
  public <T> Builder realObject(@NotNull Class<T> objectClass) {
    mBuilder.realObject(objectClass);
    return this;
  }

  @NotNull
  @Override
  public <T> Builder realObject(@NotNull TypeToken<T> objectToken) {
    mBuilder.realObject(objectToken);
    return this;
  }

  @NotNull
  @Override
  public <T> Builder realObject(@NotNull DependencyKey<T> keyAndImplementation) {
    mBuilder.realObject(keyAndImplementation);
    return this;
  }

  @NotNull
  @Override
  public <T> Builder realObject(@NotNull DependencyKey<T> key, @NotNull Class<? extends T> implementationClass) {
    mBuilder.realObject(key, implementationClass);
    return this;
  }

  @NotNull
  @Override
  public <T> Builder realObject(@NotNull DependencyKey<T> key, @NotNull TypeToken<? extends T> implementationToken) {
    mBuilder.realObject(key, implementationToken);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso build() {
    return this;
  }

  @NotNull
  @Override
  public Rule buildRule() {
    throw new VerifyError("Can't build a new mockspresso @Rule on top of an existing one.");
  }
}
