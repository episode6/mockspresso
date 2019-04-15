package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.mockito.MockitoAnnotations;

/**
 *
 * @deprecated This functionality is internal and should not be exposed.
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class MockitoFieldPreparer implements MockerConfig.FieldPreparer {

  @Override
  public void prepareFields(Object objectWithMockFields) {
    MockitoAnnotations.initMocks(objectWithMockFields);
  }
}
