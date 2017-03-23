package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.easymock.EasyMock;

/**
 * A MockMaker for EasyMock
 */
public class EasyMockMockMaker implements MockerConfig.MockMaker {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    // Using a strict mock by default here doesn't make sense for an auto-mocker
    return EasyMock.<T>niceMock((Class<T>)typeToken.getRawType());
  }
}
