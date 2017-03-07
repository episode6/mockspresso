package com.episode6.hackit.mockspresso.junit;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.LinkedList;
import java.util.List;

/**
 * Similar to java's {@link org.junit.rules.RuleChain} except that it is implemented via
 * a MethodRule instead of a TestRule, and it supports both MethodRules and TestRules (but
 * not ClassRules)
 *
 * Currently this isn't directly tied to Mockspresso, so I may pull this out and
 * move to its own library.
 */
public class MethodRuleChain implements MethodRule {
  private static final MethodRuleChain EMPTY_CHAIN = new MethodRuleChain(new LinkedList<MethodRule>());

  private List<MethodRule> rulesStartingWithInnerMost;

  public static MethodRuleChain emptyRuleChain() {
    return EMPTY_CHAIN;
  }

  public static MethodRuleChain outerRule(MethodRule outerRule) {
    return emptyRuleChain().around(outerRule);
  }

  public static MethodRuleChain outerRule(TestRule outerRule) {
    return outerRule(MethodRules.wrapTestRule(outerRule));
  }

  private MethodRuleChain(List<MethodRule> rules) {
    this.rulesStartingWithInnerMost = rules;
  }

  public MethodRuleChain around(MethodRule enclosedRule) {
    List<MethodRule> rulesOfNewChain = new LinkedList<>();
    rulesOfNewChain.add(enclosedRule);
    rulesOfNewChain.addAll(rulesStartingWithInnerMost);
    return new MethodRuleChain(rulesOfNewChain);
  }

  public MethodRuleChain around(TestRule enclosedRule) {
    return around(MethodRules.wrapTestRule(enclosedRule));
  }

  @Override
  public Statement apply(Statement base, FrameworkMethod method, Object target) {
    for (MethodRule rule : rulesStartingWithInnerMost) {
      base = rule.apply(base, method, target);
    }
    return base;
  }
}
