package com.episode6.hackit.mockspresso.mockito;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.mockito.Mockito;

/**
 * A MockspressoPlugin that applies the mockito mocker config.
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByMockito()` and its
 * JavaSupport counterpart {@link MockspressoMockitoPluginsJavaSupport#mockByMockito()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class MockitoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert Mockito.class != null;
    return builder.mocker(new MockitoMockerConfig());
  }
}
