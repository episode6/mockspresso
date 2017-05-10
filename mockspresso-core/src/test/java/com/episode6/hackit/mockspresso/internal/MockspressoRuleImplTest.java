package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Provider;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Test {@link MockspressoRuleImpl}
 */
@RunWith(DefaultTestRunner.class)
public class MockspressoRuleImplTest {

  @Mock Provider<MockspressoBuilderImpl> mBuilderProvider;

  @Mock MockspressoInternal mOriginal;
  @Mock MockspressoInternal mChildMockspresso;
  @Mock MockspressoConfigContainer mConfig;
  @Mock MockspressoConfigContainer mChildConfig;

  @Mock MockspressoBuilderImpl mBuilder;
  @Mock MockspressoBuilderImpl mChildBuilder;

  @Mock FrameworkMethod mFrameworkMethod;
  @Mock Object mTarget;

  RuleConfig mRuleConfig;
  Mockspresso.Rule mRule;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mOriginal.getConfig()).thenReturn(mConfig);
    when(mChildMockspresso.getConfig()).thenReturn(mChildConfig);
    when(mBuilder.buildInternal()).thenReturn(mOriginal);

    when(mBuilderProvider.get()).thenReturn(mChildBuilder);
    when(mChildBuilder.buildInternal()).thenReturn(mChildMockspresso);

    when(mBuilder.deepCopy()).thenReturn(mBuilder);
    when(mChildBuilder.deepCopy()).thenReturn(mChildBuilder);

    mRuleConfig = new RuleConfig();
    mRule = new MockspressoRuleImpl(mBuilder, mBuilderProvider, mRuleConfig);
  }

  @Test
  public void testBasicUsage() throws Throwable {
    Statement base = verifyChildDelegateStatement();

    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(mConfig, mBuilderProvider, mBuilder, base, mConfig, mChildConfig);
    inOrder.verify(mBuilder).deepCopy();
    inOrder.verify(mBuilder).testResourcesWithoutLifecycle(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(base).evaluate();
    inOrder.verify(mConfig).teardown();

    assertRuleNoLongerWorks();
  }

  @Test
  public void testEarlyBuildUponUsage() throws Throwable {
    // simulate building upon a final @Rule at the class-level
    Mockspresso mockspresso = mRule.buildUpon().build();
    Statement base = verifyChildDelegateStatement(mockspresso, mChildMockspresso);

    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(mConfig, mBuilderProvider, mBuilder, base, mConfig, mChildConfig, mChildBuilder, mChildMockspresso);
    inOrder.verify(mBuilderProvider).get();
    inOrder.verify(mBuilder).deepCopy();
    inOrder.verify(mBuilder).testResourcesWithoutLifecycle(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(mChildBuilder).deepCopy();
    inOrder.verify(mChildBuilder).buildInternal();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(base).evaluate();
    inOrder.verify(mChildConfig).teardown();
    inOrder.verify(mConfig).teardown();

    assertMockspressoNoLongerWorks(mockspresso);
    assertRuleNoLongerWorks();
  }

  @Test
  public void testChainUsage() throws Throwable {
    Statement base = verifyChildDelegateStatement();
    TestTestRule outerRule1 = spy(new TestTestRule());
    TestMethodRule outerRule2 = spy(new TestMethodRule());
    TestTestRule innerRule1 = spy(new TestTestRule());
    TestMethodRule innerRule2 = spy(new TestMethodRule());
    mRuleConfig.addOuterRule(outerRule1);
    mRuleConfig.addOuterRule(outerRule2);
    mRuleConfig.addInnerRule(innerRule1);
    mRuleConfig.addInnerRule(innerRule2);


    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(
        mConfig,
        mBuilderProvider,
        mBuilder,
        base,
        mConfig,
        innerRule1,
        innerRule1.returnStatement,
        innerRule2,
        innerRule2.returnStatement,
        outerRule1,
        outerRule1.returnStatement,
        outerRule2,
        outerRule2.returnStatement);

    // apply happens in reverse order
    inOrder.verify(innerRule2).apply(base, mFrameworkMethod, mTarget);
    inOrder.verify(innerRule1).apply(eq(innerRule2.returnStatement), any(Description.class));
    inOrder.verify(outerRule2).apply(any(Statement.class), eq(mFrameworkMethod), eq(mTarget));
    inOrder.verify(outerRule1).apply(eq(outerRule2.returnStatement), any(Description.class));

    // eval happens in correct order, first outerRule1, outerRule2, then mockspresso,
    // then innerRule1, then innerRule2, then base
    inOrder.verify(outerRule1.returnStatement).before();
    inOrder.verify(outerRule2.returnStatement).before();
    inOrder.verify(mBuilder).deepCopy();
    inOrder.verify(mBuilder).testResourcesWithoutLifecycle(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(innerRule1.returnStatement).before();
    inOrder.verify(innerRule2.returnStatement).before();
    inOrder.verify(base).evaluate();
    inOrder.verify(innerRule2.returnStatement).after();
    inOrder.verify(innerRule1.returnStatement).after();
    inOrder.verify(mConfig).teardown();
    inOrder.verify(outerRule2.returnStatement).after();
    inOrder.verify(outerRule1.returnStatement).after();

    assertRuleNoLongerWorks();
  }

  @Test
  public void testChainUsageWithChildInstance() throws Throwable {
    // simulate building upon a final @Rule at the class-level
    Mockspresso mockspresso = mRule.buildUpon().build();
    Statement base = verifyChildDelegateStatement(mockspresso, mChildMockspresso);
    TestTestRule outerRule1 = spy(new TestTestRule());
    TestMethodRule outerRule2 = spy(new TestMethodRule());
    TestTestRule innerRule1 = spy(new TestTestRule());
    TestMethodRule innerRule2 = spy(new TestMethodRule());
    mRuleConfig.addOuterRule(outerRule1);
    mRuleConfig.addOuterRule(outerRule2);
    mRuleConfig.addInnerRule(innerRule1);
    mRuleConfig.addInnerRule(innerRule2);

    Statement result = mRule.apply(base, mFrameworkMethod, mTarget);
    result.evaluate();

    InOrder inOrder = Mockito.inOrder(
        mConfig,
        mBuilderProvider,
        mBuilder,
        base,
        mConfig,
        mChildConfig,
        innerRule1,
        innerRule1.returnStatement,
        innerRule2,
        innerRule2.returnStatement,
        mChildBuilder,
        outerRule1,
        outerRule1.returnStatement,
        outerRule2,
        outerRule2.returnStatement);

    // first builderProvider.get happens inline
    inOrder.verify(mBuilderProvider).get();

    // apply happens in reverse order
    inOrder.verify(innerRule2).apply(base, mFrameworkMethod, mTarget);
    inOrder.verify(innerRule1).apply(eq(innerRule2.returnStatement), any(Description.class));
    inOrder.verify(outerRule2).apply(any(Statement.class), eq(mFrameworkMethod), eq(mTarget));
    inOrder.verify(outerRule1).apply(eq(outerRule2.returnStatement), any(Description.class));

    // eval happens in correct order, first outerRules, then mockspresso, then innerRule1, then innerRule2, then base
    inOrder.verify(outerRule1.returnStatement).before();
    inOrder.verify(outerRule2.returnStatement).before();
    inOrder.verify(mBuilder).deepCopy();
    inOrder.verify(mBuilder).testResourcesWithoutLifecycle(mTarget);
    inOrder.verify(mBuilder).buildInternal();
    inOrder.verify(mConfig).setup(mOriginal);
    inOrder.verify(mChildBuilder).deepCopy();
    inOrder.verify(mChildBuilder).buildInternal();
    inOrder.verify(mChildConfig).setup(mChildMockspresso);
    inOrder.verify(innerRule1.returnStatement).before();
    inOrder.verify(innerRule2.returnStatement).before();
    inOrder.verify(base).evaluate();
    inOrder.verify(innerRule2.returnStatement).after();
    inOrder.verify(innerRule1.returnStatement).after();
    inOrder.verify(mChildConfig).teardown();
    inOrder.verify(mConfig).teardown();
    inOrder.verify(outerRule2.returnStatement).after();
    inOrder.verify(outerRule1.returnStatement).after();

    assertRuleNoLongerWorks();
  }

  // create a (spy) Statement that, when evaluated, makes some calls to mRule and verifies
  // that mChildMockspresso is called as a delegate.
  private Statement verifyChildDelegateStatement() {
    return verifyChildDelegateStatement(mRule, mOriginal);
  }

  private Statement verifyChildDelegateStatement(final Mockspresso instanceToCall, final Mockspresso delegateToVerify) {
    return spy(new Statement() {
      @Override
      public void evaluate() throws Throwable {
        instanceToCall.create(String.class);
        instanceToCall.create(TypeToken.of(Integer.class));
        instanceToCall.buildUpon();

        InOrder inOrder = Mockito.inOrder(delegateToVerify);
        inOrder.verify(delegateToVerify).create(String.class);
        inOrder.verify(delegateToVerify).create(TypeToken.of(Integer.class));
        inOrder.verify(delegateToVerify).buildUpon();
      }
    });
  }

  private void assertRuleNoLongerWorks() {
    assertMockspressoNoLongerWorks(mRule);
  }

  private void assertMockspressoNoLongerWorks(Mockspresso instance) {
    try {
      instance.create(String.class);
      fail("Expected a NullPointerException");
    } catch (NullPointerException e) {
      // pass
    }
    try {
      instance.create(TypeToken.of(Integer.class));
      fail("Expected a NullPointerException");
    } catch (NullPointerException e) {
      // pass
    }
  }

  private static class TestClass {}

  private static class TestStatement extends Statement {
    private final Statement mBaseStatement;
    private TestStatement(Statement baseStatement) {
      mBaseStatement = baseStatement;
    }

    public void before() {} // spy
    public void after() {} // spy

    @Override
    public void evaluate() throws Throwable {
      before();
      mBaseStatement.evaluate();
      after();
    }
  }

  private static class TestTestRule implements TestRule {
    TestStatement returnStatement;

    @Override
    public Statement apply(Statement base, Description description) {
      returnStatement = spy(new TestStatement(base));
      return returnStatement;
    }
  }

  private static class TestMethodRule implements MethodRule {
    TestStatement returnStatement;

    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
      returnStatement = spy(new TestStatement(base));
      return returnStatement;
    }
  }

  private static class BuilderAnswer implements Answer<Object> {

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      Class<?> returnType = invocation.getMethod().getReturnType();
      if (returnType == Mockspresso.Builder.class ||
          returnType == Mockspresso.class) {
        return invocation.getMock();
      }
      return Answers.RETURNS_DEFAULTS.answer(invocation);
    }
  }
}
