package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {
  <T> T create(Class<T> clazz);
  <T> T create(TypeToken<T> typeToken);
  Builder buildUpon();

  interface Rule extends Mockspresso, MethodRule {}

  interface Builder {
    Builder plugin(MockspressoPlugin plugin);
    Builder fieldsFrom(Object objectWithFields);
    Builder mockerConfig(MockerConfig mockerConfig);
    Builder injectionConfig(InjectionConfig injectionConfig);
    Builder specialObjectMaker(SpecialObjectMaker specialObjectMaker);
    Builder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers);

    <T> Builder dependency(Class<T> clazz, T value);
    <T> Builder dependency(TypeToken<T> typeToken, T value);
    <T> Builder dependency(Class<T> clazz, Annotation annotation, T value);
    <T> Builder dependency(TypeToken<T> typeToken, Annotation annotation, T value);

    Mockspresso build();
    Rule buildRule();
  }

  class Builders {
    public static Builder empty() {
      return new com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl();
    }
    public static Builder simple() {
      return empty()
          .plugin(com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin.getInstance());
    }
    public static Builder javaxInjection() {
      return empty()
          .plugin(com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin.getInstance());
    }
  }
}
