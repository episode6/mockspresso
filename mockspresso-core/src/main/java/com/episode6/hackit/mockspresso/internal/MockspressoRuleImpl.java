package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.MockspressoBuilder;
import com.episode6.hackit.mockspresso.MockspressoRule;
import com.episode6.hackit.mockspresso.internal.MockspressoImpl;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * The implementation of {@link MockspressoRule}. Hold an instance of the
 * {@link MockspressoBuilder} and builds a new instance of Mockspress for
 * every test.
 **/
public class MockspressoRuleImpl implements MockspressoRule {

  private final MockspressoBuilder mBuilder;
  private Mockspresso mDelegate = null;

  public MockspressoRuleImpl(MockspressoBuilder builder) {
    mBuilder = builder;
  }

  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        mDelegate = mBuilder.build();
        base.evaluate();
      }
    };
  }

  @Override
  public <T> T constructRealObject(Class<T> clazz) {
    return mDelegate.constructRealObject(clazz);
  }

  @Override
  public <T> T constructRealObject(TypeToken<T> typeToken) {
    return mDelegate.constructRealObject(typeToken);
  }
}
