package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.EasyMockSupport;

/**
 * A FieldPreparer for EasyMock
 */
public class EasyMockFieldPreparer implements MockerConfig.FieldPreparer {
  @Override
  public void prepareFields(Object objectWithMockFields) {
    EasyMockSupport.injectMocks(objectWithMockFields);
  }
}
