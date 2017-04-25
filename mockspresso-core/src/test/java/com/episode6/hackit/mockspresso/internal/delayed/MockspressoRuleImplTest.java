package com.episode6.hackit.mockspresso.internal.delayed;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl;
import com.episode6.hackit.mockspresso.internal.MockspressoConfigContainer;
import com.episode6.hackit.mockspresso.internal.MockspressoInternal;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Test {@link MockspressoRuleImpl}
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoRuleImplTest {

  @Mock MockspressoInternal mOriginal;
  @Mock MockspressoInternal mChildMockspresso;
  @Mock MockspressoConfigContainer mConfig;
  @Mock MockspressoConfigContainer mChildConfig;

  @Mock MockspressoBuilderImpl mBuilder;

  @Mock FrameworkMethod mFrameworkMethod;
  @Mock Object mTarget;

  Mockspresso.Rule mRule;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mOriginal.getConfig()).thenReturn(mConfig);
    when(mChildMockspresso.getConfig()).thenReturn(mChildConfig);
    when(mConfig.newBuilder()).thenReturn(mBuilder);
    when(mBuilder.buildInternal()).thenReturn(mChildMockspresso);

    mRule = new MockspressoRuleImpl(mOriginal);
  }

  @Test
  public void testBasicUsage() throws Throwable {
    Statement base = verifyChildDelegateStatement();

    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(mConfig, mBuilder, base, mConfig, mChildConfig);
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(mConfig).newBuilder();
    inOrder.verify(mBuilder).fieldsFrom(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(base).evaluate();
    inOrder.verify(mChildConfig).teardown();
    inOrder.verify(mConfig).teardown();

    assertRuleNoLongerWorks();
  }

  @Test
  public void testChainUsage() throws Throwable {
    Statement base = verifyChildDelegateStatement();
    TestTestRule innerRule1 = spy(new TestTestRule());
    TestMethodRule innerRule2 = spy(new TestMethodRule());

    mRule = mRule.chainAround(innerRule1).chainAround(innerRule2);

    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(
        mConfig,
        mBuilder,
        base,
        mConfig,
        mChildConfig,
        innerRule1,
        innerRule1.returnStatement,
        innerRule2,
        innerRule2.returnStatement);

    // apply happens in reverse order
    inOrder.verify(innerRule2).apply(base, mFrameworkMethod, mTarget);
    inOrder.verify(innerRule1).apply(eq(innerRule2.returnStatement), any(Description.class));

    // eval happens in correct order, first mockspresso, then innerRule1, then innerRule2, then base
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(mConfig).newBuilder();
    inOrder.verify(mBuilder).fieldsFrom(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(innerRule1.returnStatement).evaluate();
    inOrder.verify(innerRule2.returnStatement).evaluate();
    inOrder.verify(base).evaluate();
    inOrder.verify(mChildConfig).teardown();
    inOrder.verify(mConfig).teardown();

    assertRuleNoLongerWorks();
  }

  // create a (spy) Statement that, when evaluated, makes some calls to mRule and verifies
  // that mChildMockspresso is called as a delegate.
  private Statement verifyChildDelegateStatement() {
    return spy(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        mRule.create(String.class);
        mRule.create(TypeToken.of(Integer.class));
        mRule.buildUpon();

        InOrder inOrder = Mockito.inOrder(mChildMockspresso);
        inOrder.verify(mChildMockspresso).create(String.class);
        inOrder.verify(mChildMockspresso).create(TypeToken.of(Integer.class));
        inOrder.verify(mChildMockspresso).buildUpon();
      }
    });
  }

  private void assertRuleNoLongerWorks() {
    try {
      mRule.create(String.class);
      fail("Expected a NullPointerException");
    } catch (NullPointerException e) {
      // pass
    }
    try {
      mRule.create(TypeToken.of(Integer.class));
      fail("Expected a NullPointerException");
    } catch (NullPointerException e) {
      // pass
    }
  }

  private class TestTestRule implements TestRule {

    Statement returnStatement;

    @Override
    public Statement apply(final Statement base, Description description) {
      returnStatement = spy(new Statement() {
        @Override
        public void evaluate() throws Throwable {
          base.evaluate();
        }
      });
      return returnStatement;
    }
  }

  private class TestMethodRule implements MethodRule {

    Statement returnStatement;

    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
      returnStatement = spy(new Statement() {
        @Override
        public void evaluate() throws Throwable {
          base.evaluate();
        }
      });
      return returnStatement;
    }
  }
}
