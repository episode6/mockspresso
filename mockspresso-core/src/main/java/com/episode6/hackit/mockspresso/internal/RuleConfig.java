package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.junit.MethodRuleChain;
import com.episode6.hackit.mockspresso.junit.MethodRules;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds onto outer rules and inner rules, used for chaining a Mockspresso.Rule
 */
class RuleConfig {
  private final List<MethodRule> mOuterRules;
  private final List<MethodRule> mInnerRules;

  public RuleConfig() {
    this(new LinkedList<MethodRule>(), new LinkedList<MethodRule>());
  }

  private RuleConfig(List<MethodRule> outerRules, List<MethodRule> innerRules) {
    mOuterRules = outerRules;
    mInnerRules = innerRules;
  }

  RuleConfig deepCopy() {
    return new RuleConfig(
        new LinkedList<MethodRule>(mOuterRules),
        new LinkedList<MethodRule>(mInnerRules));
  }

  boolean isEmpty() {
    return mOuterRules.isEmpty() && mInnerRules.isEmpty();
  }

  void addOuterRule(TestRule testRule) {
    mOuterRules.add(MethodRules.wrapTestRule(testRule));
  }

  void addOuterRule(MethodRule methodRule) {
    mOuterRules.add(methodRule);
  }

  void addInnerRule(TestRule testRule) {
    mInnerRules.add(MethodRules.wrapTestRule(testRule));
  }

  void addInnerRule(MethodRule methodRule) {
    mInnerRules.add(methodRule);
  }

  MethodRuleChain buildRuleChain(MethodRule centerRule) {
    MethodRuleChain chain = MethodRuleChain.emptyRuleChain();
    for (MethodRule rule : mOuterRules) {
      chain = chain.chainAround(rule);
    }
    chain = chain.chainAround(centerRule);
    for (MethodRule rule : mInnerRules) {
      chain = chain.chainAround(rule);
    }
    return chain;
  }
}
