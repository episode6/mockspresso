package com.episode6.hackit.mockspresso.junit;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Utility methods for Method Rules
 */
public class MethodRules {

  /**
   * Wrap a {@link TestRule} in a {@link MethodRule} for compatibility.
   * @param testRule The {@link TestRule} to wrap
   * @return a {@link MethodRule} that will execute the provided testRule when applied
   */
  public static MethodRule wrapTestRule(TestRule testRule) {
    return new MethodRuleWrapper(testRule);
  }

  private static class MethodRuleWrapper implements MethodRule {
    private final TestRule mTestRule;

    private MethodRuleWrapper(TestRule testRule) {
      mTestRule = testRule;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
      final Description description = Description.createTestDescription(target.getClass(), method.getName());
      return mTestRule.apply(base, description);
    }
  }
}
