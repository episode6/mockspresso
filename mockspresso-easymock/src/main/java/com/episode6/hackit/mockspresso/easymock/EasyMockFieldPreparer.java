package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.EasyMockSupport;

/**
 * A FieldPreparer for EasyMock
 *
 * @deprecated This functionality is internal implementation and shouldn't be exposed
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class EasyMockFieldPreparer implements MockerConfig.FieldPreparer {
  @Override
  public void prepareFields(Object objectWithMockFields) {
    EasyMockSupport.injectMocks(objectWithMockFields);
  }
}
