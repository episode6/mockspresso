package com.episode6.hackit.mockspresso.api;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An abstract implementation of MockerConfig
 */
public abstract class AbstractMockerConfig implements MockerConfig {

  private final MockMaker mMockMaker;
  private final FieldPreparer mFieldPreparer;

  protected AbstractMockerConfig(MockMaker mockMaker, FieldPreparer fieldPreparer) {
    mMockMaker = mockMaker;
    mFieldPreparer = fieldPreparer;
  }

  @Override
  public MockMaker provideMockMaker() {
    return mMockMaker;
  }

  @Override
  public FieldPreparer provideFieldPreparer() {
    return mFieldPreparer;
  }
}
