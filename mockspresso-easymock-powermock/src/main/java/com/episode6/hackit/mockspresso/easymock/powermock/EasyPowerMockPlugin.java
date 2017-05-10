package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

/**
 * Plugin that applies {@link EasyPowerMockMockerConfig} to enable mockspresso usage with Powermock + EasyMock.
 * This plugin does not apply a PowerMockRule, so the implementer is responsible for either running their
 * test with the PowerMockRunner or applying their own PowerMockRule
 */
public class EasyPowerMockPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert EasyMock.class != null;
    assert PowerMock.class != null;
    return builder.mocker(new EasyPowerMockMockerConfig());
  }
}
