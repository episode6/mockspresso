package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import javax.inject.Provider;

/**
 * The implementation of {@link Mockspresso.Rule}. Hold an instance of the
 * {@link Mockspresso} and buildsUpon it for each test.
 **/
class MockspressoRuleImpl extends AbstractDelayedMockspresso implements Mockspresso.Rule {

  private final MockspressoBuilderImpl mOriginalBuilder;
  private final RuleConfig mRuleConfig;

  MockspressoRuleImpl(
      MockspressoBuilderImpl originalBuilder,
      Provider<MockspressoBuilderImpl> builderProvider,
      RuleConfig ruleConfig) {
    super(builderProvider);
    mOriginalBuilder = originalBuilder;
    mRuleConfig = ruleConfig;
  }

  @Override
  public Statement apply(Statement base, FrameworkMethod method, Object target) {
    return mRuleConfig.buildRuleChain(new ImplRule()).apply(base, method, target);
  }

  private class ImplRule implements MethodRule {
    @Override
    public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          try {
            MockspressoBuilderImpl builder = mOriginalBuilder.deepCopy();
            builder.testResourcesWithoutLifecycle(target);
            setDelegate(builder.buildInternal());
            base.evaluate();
          } finally {
            setDelegate(null);
          }
        }
      };
    }
  }
}
