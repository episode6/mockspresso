package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.mockito.Mockito;

/**
 * A {@link com.episode6.hackit.mockspresso.api.MockerConfig.MockMaker} for Mockito
 *
 * @deprecated This functionality is internal and should not be exposed.
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class MockitoMockMaker implements MockerConfig.MockMaker {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    return Mockito.<T>mock((Class<T>) typeToken.getRawType());
  }
}
