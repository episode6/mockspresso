package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.Mock;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * A MockerConfig for PowerMock + EasyMock
 */
public class EasyPowerMockMockerConfig implements MockerConfig {

  private final EasyPowerMockMockMaker mMockMaker = new EasyPowerMockMockMaker();
  private final EasyPowerMockFieldPreparer mFieldPreparer = new EasyPowerMockFieldPreparer();

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
