package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Plugin that applies {@link PowerMockitoConfig} AND applies a PowerMockRule as an outer rule
 * to Mockspresso. Use this plugin if you want to use PowerMock without applying the PowerMockRunner
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByPowerMockitoRule()` and its
 * JavaSupport counterpart {@link MockspressoPowerMockitoPluginsJavaSupport#mockByPowerMockitoRule()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class PowerMockitoRulePlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.plugin(new PowerMockitoPlugin())
        .outerRule(new PowerMockRule());
  }
}
