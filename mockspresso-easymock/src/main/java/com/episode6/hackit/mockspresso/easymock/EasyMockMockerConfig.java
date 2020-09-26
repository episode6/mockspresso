package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
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
public class EasyMockMockerConfig implements MockerConfig, MockerConfig.MockMaker, MockerConfig.FieldPreparer {

  @Override
  public MockMaker provideMockMaker() {
    return this;
  }

  @Override
  public FieldPreparer provideFieldPreparer() {
    return this;
  }

  @Override
  public List<Class<? extends Annotation>> provideMockAnnotations() {
    return Collections.<Class<? extends Annotation>>singletonList(Mock.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeMock(TypeToken<T> typeToken) {
    // Using a strict mock by default here doesn't make sense for an auto-mocker
    T obj = EasyMock.<T>niceMock((Class<T>)typeToken.getRawType());

    // Since no one holds a reference to these mocks (besides the real object being
    // injected), they should be returned already in replay mode.
    EasyMock.replay(obj);
    return obj;
  }

  @Override
  public void prepareFields(Object objectWithMockFields) {
    EasyMockSupport.injectMocks(objectWithMockFields);
  }
}
