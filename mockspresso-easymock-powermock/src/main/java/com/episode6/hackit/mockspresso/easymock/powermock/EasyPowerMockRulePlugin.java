package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.powermock.modules.junit4.rule.PowerMockRule;

/**
 * Plugin that applies {@link EasyPowerMockMockerConfig} AND applies a PowerMockRule as an outer rule
 * to Mockspresso. Use this plugin if you want to use PowerMock without applying the PowerMockRunner
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `mockByPowerMockRule()` and its
 * JavaSupport counterpart {@link MockspressoEasyPowerMockPluginsJavaSupport#mockByPowerMockRule()}
 *
 * This class will be removed in a future release
 */
@Deprecated
public class EasyPowerMockRulePlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    return builder.plugin(new EasyPowerMockPlugin())
        .outerRule(new PowerMockRule());
  }
}
