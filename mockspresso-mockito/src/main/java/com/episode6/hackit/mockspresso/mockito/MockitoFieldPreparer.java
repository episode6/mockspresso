package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.mockito.MockitoAnnotations;

/**
 *
 */
public class MockitoFieldPreparer implements MockerConfig.FieldPreparer {

  @Override
  public void prepareFields(Object objectWithMockFields) {
    MockitoAnnotations.initMocks(objectWithMockFields);
  }
}
