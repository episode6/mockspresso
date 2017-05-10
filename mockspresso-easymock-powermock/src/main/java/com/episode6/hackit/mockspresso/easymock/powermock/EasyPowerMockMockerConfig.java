package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.Mock;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.api.easymock.annotation.MockStrict;

import java.lang.annotation.Annotation;
import java.util.Arrays;
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
    return Arrays.asList(
        Mock.class,
        org.powermock.api.easymock.annotation.Mock.class,
        MockStrict.class,
        MockNice.class);
  }
}
