package com.episode6.hackit.mockspresso.internal.delayed;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.internal.MockspressoConfigContainer;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.List;

/**
 * An implementation of {@link Mockspresso.Builder} that also acts as its own Mockspresso instance.
 * This allows us to buildUpon an "incomplete" @Rule, and as long as none of the 'create' methods are
 * called before the rule has a statement applied to it, everything should still work.
 */
public class DelayedMockspressoBuilder extends AbstractDelayedMockspresso implements Mockspresso.Builder {

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
      mBuilder.setParent(parentConfig);
      setDelegate(mBuilder.buildInternal());
    }
  }

  @Override
  public Builder plugin(MockspressoPlugin plugin) {
    mBuilder.plugin(plugin);
    return this;
  }

  @Override
  public Builder fieldsFrom(Object objectWithFields) {
    mBuilder.fieldsFrom(objectWithFields);
    return this;
  }

  @Override
  public Builder initializer(MockspressoInitializer initializer) {
    mBuilder.initializer(initializer);
    return this;
  }

  @Override
  public Builder initializerWithFields(MockspressoInitializer initializerWithFields) {
    mBuilder.initializerWithFields(initializerWithFields);
    return this;
  }

  @Override
  public Builder mockerConfig(MockerConfig mockerConfig) {
    mBuilder.mockerConfig(mockerConfig);
    return this;
  }

  @Override
  public Builder injectionConfig(InjectionConfig injectionConfig) {
    mBuilder.injectionConfig(injectionConfig);
    return this;
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
