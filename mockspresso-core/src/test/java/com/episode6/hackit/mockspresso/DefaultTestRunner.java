package com.episode6.hackit.mockspresso;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Default test runner for our tests
 */
public class DefaultTestRunner extends BlockJUnit4ClassRunner {

  public DefaultTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }
}
