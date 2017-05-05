package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.Mock;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * A MockerConfig for EasyMock
 */
public class EasyMockMockerConfig implements MockerConfig {

  // This object has no state, so we maintain a static instance of it
  // instead of creating multiple instances on the fly
  private static final EasyMockMockerConfig INSTANCE = new EasyMockMockerConfig();
  public static EasyMockMockerConfig getInstance() {
    return INSTANCE;
  }

  private final EasyMockMockMaker mMockMaker = new EasyMockMockMaker();
  private final EasyMockFieldPreparer mFieldPreparer = new EasyMockFieldPreparer();

  @Override
  public MockMaker provideMockMaker() {
    return mMockMaker;
  }

  @Override
  public FieldPreparer provideFieldPreparer() {
    return mFieldPreparer;
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Collections.<Class<? extends Annotation>>singletonList(Mock.class);
  }
}
