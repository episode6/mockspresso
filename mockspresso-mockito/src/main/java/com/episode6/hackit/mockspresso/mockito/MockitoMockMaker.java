package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.mockito.Mockito;

/**
 * A {@link com.episode6.hackit.mockspresso.api.MockerConfig.MockMaker} for Mockito
 */
public class MockitoMockMaker implements MockerConfig.MockMaker {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    return Mockito.<T>mock((Class<T>) typeToken.getRawType());
  }
}
