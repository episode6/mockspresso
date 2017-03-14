package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The implementation of {@link Mockspresso.Rule}. Hold an instance of the
 * {@link Mockspresso} and buildsUpon it for each test.
 **/
public class MockspressoRuleImpl extends AbstractDelayedMockspresso implements Mockspresso.Rule {

  private final MockspressoInternal mOriginal;

  public MockspressoRuleImpl(MockspressoInternal original) {
    mOriginal = original;
  }

  @Override
  public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        MockspressoBuilderImpl builder = new MockspressoBuilderImpl(mOriginal.getConfig());
        builder.fieldsFrom(target);
        setDelegate(builder.buildInternal());
        base.evaluate();
        setDelegate(null);
      }
    };
  }
}
