package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * The implementation of {@link Mockspresso.Rule}. Hold an instance of the
 * {@link Mockspresso.Builder} and builds a new instance of Mockspresso for
 * every test.
 **/
public class MockspressoRuleImpl implements Mockspresso.Rule {

  private final Mockspresso.Builder mBuilder;
  private Mockspresso mDelegate = null;

  public MockspressoRuleImpl(Mockspresso.Builder builder) {
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
  public <T> T create(Class<T> clazz) {
    return mDelegate.create(clazz);
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return mDelegate.create(typeToken);
  }

  @Override
  public Builder buildUpon() {
    return mDelegate.buildUpon();
  }
}
