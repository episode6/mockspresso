package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {
  <T> T create(Class<T> clazz);
  <T> T create(TypeToken<T> typeToken);
  Builder buildUpon();

  interface Rule extends Mockspresso, MethodRule {}

  interface Builder {
    Builder parent(Mockspresso mockspresso);
    Builder fieldsFrom(Object objectWithFields);

    Mockspresso build();
    Rule buildRule();
  }

  class Builders {
    static Builder simple() {
      return new MockspressoBuilderImpl();
    }
  }
}
