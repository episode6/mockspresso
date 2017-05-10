package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

/**
 * A MockMaker for PowerMock + EasyMock
 */
public class EasyPowerMockMockMaker implements MockerConfig.MockMaker {

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    // Using a strict mock by default here doesn't make sense for an auto-mocker
    T obj = PowerMock.<T>createNiceMock((Class<T>)typeToken.getRawType());

    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(obj);
    return obj;
  }
}
