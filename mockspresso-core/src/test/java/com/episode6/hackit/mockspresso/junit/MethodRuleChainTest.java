package com.episode6.hackit.mockspresso.junit;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link MethodRuleChain}
 */
@RunWith(DefaultTestRunner.class)
public class MethodRuleChainTest {

  @Mock Statement baseStatement;
  @Mock FrameworkMethod mockFrameworkMethod;
  @Mock Object mockTestObject;

  AutoCloseable mockitoClosable;

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);
  }

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Test
  public void testBasicChain() throws Throwable {
    MethodRuleInfo outerRule = new MethodRuleInfo();
    MethodRuleInfo middleRule = new MethodRuleInfo();
    MethodRuleInfo innerRule = new MethodRuleInfo();

    MethodRuleChain chain = MethodRuleChain.outerRule(outerRule.methodRule)
        .chainAround(middleRule.methodRule)
        .chainAround(innerRule.methodRule);

    Statement resultStatement = chain.apply(baseStatement, mockFrameworkMethod, mockTestObject);
    resultStatement.evaluate();

    InOrder inOrder = Mockito.inOrder(collectMocks(baseStatement, outerRule, middleRule, innerRule));
    inOrder.verify(innerRule.methodRule).apply(any(Statement.class), any(FrameworkMethod.class), any(Object.class));
    inOrder.verify(middleRule.methodRule).apply(any(Statement.class), any(FrameworkMethod.class), any(Object.class));
    inOrder.verify(outerRule.methodRule).apply(any(Statement.class), any(FrameworkMethod.class), any(Object.class));

    inOrder.verify(outerRule.beforeRunnable).run();
    inOrder.verify(middleRule.beforeRunnable).run();
    inOrder.verify(innerRule.beforeRunnable).run();
    inOrder.verify(baseStatement).evaluate();
    inOrder.verify(innerRule.afterRunnable).run();
    inOrder.verify(middleRule.afterRunnable).run();
    inOrder.verify(outerRule.afterRunnable).run();

    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testTestRuleChain() throws Throwable {
    TestRuleInfo outerRule = new TestRuleInfo();
    TestRuleInfo middleRule = new TestRuleInfo();
    TestRuleInfo innerRule = new TestRuleInfo();

    MethodRuleChain chain = MethodRuleChain.outerRule(outerRule.testRule)
        .chainAround(middleRule.testRule)
        .chainAround(innerRule.testRule);

    Statement resultStatement = chain.apply(baseStatement, mockFrameworkMethod, mockTestObject);
    resultStatement.evaluate();

    InOrder inOrder = Mockito.inOrder(collectMocks(baseStatement, outerRule, middleRule, innerRule));
    inOrder.verify(innerRule.testRule).apply(any(Statement.class), any(Description.class));
    inOrder.verify(middleRule.testRule).apply(any(Statement.class), any(Description.class));
    inOrder.verify(outerRule.testRule).apply(any(Statement.class), any(Description.class));

    inOrder.verify(outerRule.beforeRunnable).run();
    inOrder.verify(middleRule.beforeRunnable).run();
    inOrder.verify(innerRule.beforeRunnable).run();
    inOrder.verify(baseStatement).evaluate();
    inOrder.verify(innerRule.afterRunnable).run();
    inOrder.verify(middleRule.afterRunnable).run();
    inOrder.verify(outerRule.afterRunnable).run();

    inOrder.verifyNoMoreInteractions();
  }

  private static class RuleInfo {
    final Runnable beforeRunnable;
    final Runnable afterRunnable;

    RuleInfo() {
      beforeRunnable = mock(Runnable.class);
      afterRunnable = mock(Runnable.class);
    }
  }

  private static class MethodRuleInfo extends RuleInfo {
    final MethodRule methodRule;

    MethodRuleInfo() {
      super();
      methodRule = mock(MethodRule.class);
      when(methodRule.apply(
          any(Statement.class),
          any(FrameworkMethod.class),
          any(Object.class))).then(
          ruleApplyAnswer(beforeRunnable, afterRunnable));
    }
  }

  private static class TestRuleInfo extends RuleInfo {
    final TestRule testRule;

    TestRuleInfo() {
      super();
      testRule = mock(TestRule.class);
      when(testRule.apply(
          any(Statement.class),
          any(Description.class))).then(
          ruleApplyAnswer(beforeRunnable, afterRunnable));
    }
  }

  private static Answer<Statement> ruleApplyAnswer(final Runnable beforeRunnable, final Runnable afterRunnable) {
    return new Answer<Statement>() {
      @Override
      public Statement answer(InvocationOnMock invocation) throws Throwable {
        final Statement base = invocation.getArgument(0);
        return new Statement() {
          @Override
          public void evaluate() throws Throwable {
            beforeRunnable.run();
            base.evaluate();
            afterRunnable.run();
          }
        };
      }
    };
  }

  private static Object[] collectMocks(Object... objects) {
    ArrayList<Object> mocks = new ArrayList<>();
    for (Object o : objects) {
      if (Mockito.mockingDetails(o).isMock()) {
        mocks.add(o);
        continue;
      }
      if (o instanceof RuleInfo) {
        mocks.add(((RuleInfo) o).afterRunnable);
        mocks.add(((RuleInfo) o).beforeRunnable);
      }
      if (o instanceof MethodRuleInfo) {
        mocks.add(((MethodRuleInfo) o).methodRule);
      }
      if (o instanceof TestRuleInfo) {
        mocks.add(((TestRuleInfo) o).testRule);
      }
    }
    return mocks.toArray();
  }
}
