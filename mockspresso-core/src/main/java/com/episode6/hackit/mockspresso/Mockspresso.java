package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.TestRule;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {
  <T> T create(Class<T> clazz);
  <T> T create(TypeToken<T> typeToken);
  Builder buildUpon();

  interface Rule extends Mockspresso,TestRule {}

  interface Builder {
    Builder fieldsFrom(Object objectWithFields);

    Mockspresso build();
    Rule buildRule();
  }

  class Builders {
    static Builder fromTest(Object testObjectWithFields) {
      return new MockspressoBuilderImpl(null).fieldsFrom(testObjectWithFields);
    }
  }
}
