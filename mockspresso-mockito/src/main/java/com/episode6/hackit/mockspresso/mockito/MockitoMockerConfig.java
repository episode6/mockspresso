package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.mockito.Mock;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * A MockerConfig for Mockito.
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByMockito()` and its
 * JavaSupport counterpart {@link MockspressoMockitoPluginsJavaSupport#mockByMockito()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class MockitoMockerConfig implements MockerConfig {

  private final MockitoMockMaker mMockMaker = new MockitoMockMaker();
  private final MockitoFieldPreparer mFieldPreparer = new MockitoFieldPreparer();

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
        Spy.class);
  }

}
