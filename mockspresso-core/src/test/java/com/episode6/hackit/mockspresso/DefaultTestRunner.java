package com.episode6.hackit.mockspresso;

import com.episode6.hackit.chop.Chop;
import com.episode6.hackit.chop.tree.StdOutDebugTree;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Default test runner for our tests
 */
public class DefaultTestRunner extends BlockJUnit4ClassRunner {
  private static final Chop.Tree STD_OUT_TREE = new StdOutDebugTree();

  public DefaultTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
    Chop.plantTree(STD_OUT_TREE);
  }
}
