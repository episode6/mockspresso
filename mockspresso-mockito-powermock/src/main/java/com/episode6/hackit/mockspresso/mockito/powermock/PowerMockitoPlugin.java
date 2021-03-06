package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Plugin that applies {@link PowerMockitoConfig} to enable mockspresso usage with Powermock + Mockito.
 * This plugin does not apply a PowerMockRule, so the implementer is responsible for either running their
 * test with the PowerMockRunner or applying their own PowerMockRule
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByPowerMockito()` and its
 * JavaSupport counterpart {@link MockspressoPowerMockitoPluginsJavaSupport#mockByPowerMockito()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class PowerMockitoPlugin implements MockspressoPlugin {

  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert Mockito.class != null;
    assert PowerMockito.class != null;
    return builder.mocker(new PowerMockitoConfig());
  }
}
