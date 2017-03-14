package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 *
 */
public class DelayedMockspressoBuilder extends AbstractDelayedMockspresso implements Mockspresso.Builder {

  private final MockspressoBuilderImpl mBuilder = new MockspressoBuilderImpl();

  public void setParent(@Nullable MockspressoConfigContainer parentConfig) {
    if (parentConfig == null) {
      setDelegate(null);
    } else {
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
  public <T> Builder dependency(Class<T> clazz, T value) {
    mBuilder.dependency(clazz, value);
    return this;
  }

  @Override
  public <T> Builder dependency(TypeToken<T> typeToken, T value) {
    mBuilder.dependency(typeToken, value);
    return this;
  }

  @Override
  public <T> Builder dependency(Class<T> clazz, Annotation annotation, T value) {
    mBuilder.dependency(clazz, annotation, value);
    return this;
  }

  @Override
  public <T> Builder dependency(TypeToken<T> typeToken, Annotation annotation, T value) {
    mBuilder.dependency(typeToken, annotation, value);
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
