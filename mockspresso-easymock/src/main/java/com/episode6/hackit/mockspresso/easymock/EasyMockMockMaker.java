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
    T obj = EasyMock.<T>niceMock((Class<T>)typeToken.getRawType());

    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(obj);
    return obj;
  }
}
