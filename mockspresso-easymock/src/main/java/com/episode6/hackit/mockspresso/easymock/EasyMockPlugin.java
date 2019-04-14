package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.easymock.EasyMock;

/**
 * An implementation of MockspressoPlugin that applies the Easy mock mocker config
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByEasyMock()` and its
 * JavaSupport counterpart {@link MockspressoEasyMockPluginsJavaSupport#mockByEasyMock()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class EasyMockPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert EasyMock.class != null;
    return builder.mocker(new EasyMockMockerConfig());
  }
}
