package com.episode6.hackit.mockspresso.extend.testext;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.basic.plugin.simple.SimpleInjectMockspressoPlugin;
import com.episode6.hackit.mockspresso.extend.AbstractMockspressoExtension;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;

/**
 * Implementation of {@link TestMockspresso} extension definition. This class is totally boiler-plate except
 * for the last 2 methods in the builder ({@link Builder#simpleInjector()} and {@link Builder#mockWithMockito()}).
 */
class TestMockspressoImpl extends AbstractMockspressoExtension<TestMockspresso.Builder> implements TestMockspresso {

  TestMockspressoImpl(Mockspresso delegate) {
    super(delegate, Builder::new);
  }

  static class Rule extends AbstractMockspressoExtension.Rule<TestMockspresso.Builder> implements TestMockspresso.Rule {

    Rule(Rule delegate) {
      super(delegate, TestMockspressoImpl.Builder::new);
    }
  }

  static class Builder extends AbstractMockspressoExtension.Builder<
      TestMockspresso,
      TestMockspresso.Rule,
      TestMockspresso.Builder> implements TestMockspresso.Builder {

    Builder(Mockspresso.Builder delegate) {
      super(delegate, TestMockspressoImpl::new, Rule::new);
    }

    @Override
    public TestMockspresso.Builder simpleInjector() {
      return plugin(new SimpleInjectMockspressoPlugin());
    }

    @Override
    public TestMockspresso.Builder mockWithMockito() {
      return plugin(new MockitoPlugin());
    }
  }
}
