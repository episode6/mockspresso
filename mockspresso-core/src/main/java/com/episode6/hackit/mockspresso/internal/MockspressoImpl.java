package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

/**
 * future implementation of mockspresso functionality
 */
public class MockspressoImpl implements Mockspresso {

  private final MockspressoConfigContainer mMockspressoConfigContainer;

  MockspressoImpl(MockspressoConfigContainer mockspressoConfigContainer) {
    mMockspressoConfigContainer = mockspressoConfigContainer;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return create(TypeToken.of(clazz));
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return null;
  }

  @Override
  public Builder buildUpon() {
    return new MockspressoBuilderImpl(mMockspressoConfigContainer);
  }

}
