package com.episode6.hackit.mockspresso.internal.delayed;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.internal.MockspressoInternal;
import com.episode6.hackit.mockspresso.junit.MethodRuleChain;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The implementation of {@link Mockspresso.Rule}. Hold an instance of the
 * {@link Mockspresso} and buildsUpon it for each test.
 **/
public class MockspressoRuleImpl extends AbstractDelayedMockspresso implements Mockspresso.Rule {

  private final MockspressoInternal mOriginal;
  private MethodRuleChain mRuleChain;

  public MockspressoRuleImpl(MockspressoInternal original) {
    mOriginal = original;
    mRuleChain = MethodRuleChain.outerRule(new OuterRule());
  }

  @Override
  public Rule chainAround(TestRule testRule) {
    mRuleChain = mRuleChain.chainAround(testRule);
    return this;
  }

  @Override
  public Rule chainAround(MethodRule methodRule) {
    mRuleChain = mRuleChain.chainAround(methodRule);
    return this;
  }

  @Override
  public Statement apply(Statement base, FrameworkMethod method, Object target) {
    return mRuleChain.apply(base, method, target);
  }

  private class OuterRule implements MethodRule {
    @Override
    public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          // Create a builder for the actual mockspresso instance to be used using
          // mOriginal as the parent and adding fields from the Statement's target (the test)
          MockspressoBuilderImpl builder = mOriginal.getConfig().newBuilder();
          builder.fieldsFrom(target);

          // build the delegate instance and set it, then evaluate the Statement,
          // then clean up the delegate
          setDelegate(builder.buildInternal());
          base.evaluate();
          setDelegate(null);
        }
      };
    }
  }
}
