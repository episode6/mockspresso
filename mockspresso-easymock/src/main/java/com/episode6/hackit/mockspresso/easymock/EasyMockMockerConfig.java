package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.Mock;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * A MockerConfig for EasyMock
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByEasyMock()` and its
 * JavaSupport counterpart {@link MockspressoEasyMockPluginsJavaSupport#mockByEasyMock()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class EasyMockMockerConfig implements MockerConfig {

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
