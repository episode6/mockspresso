package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The implementation of {@link Mockspresso.Rule}. Hold an instance of the
 * {@link Mockspresso} and buildsUpon it for each test.
 **/
public class MockspressoRuleImpl implements Mockspresso.Rule {

  private final Mockspresso mOriginal;
  private Mockspresso mDelegate = null;

  public MockspressoRuleImpl(Mockspresso original) {
    mOriginal = original;
  }

  @Override
  public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        mDelegate = mOriginal.buildUpon().fieldsFrom(target).build();
        base.evaluate();
        mDelegate = null;
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
