package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.powermock.api.mockito.PowerMockito;

/**
 * Creates generic Powermock mocks
 */
public class PowerMockitoMockMaker implements MockerConfig.MockMaker {
  @Override
  @SuppressWarnings("unchecked")
  public <T> T makeMock(TypeToken<T> typeToken) {
    return PowerMockito.mock((Class<T>) typeToken.getRawType());
  }
}
