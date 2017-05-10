package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.modules.junit4.rule.PowerMockRule;

import static org.easymock.EasyMock.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyPowerMockRulePlugin}
 */
@RunWith(JUnit4.class)
public class EasyPowerMockRulePluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final EasyPowerMockRulePlugin mPlugin = new EasyPowerMockRulePlugin();

  @Before
  public void setup() {
    EasyMockSupport.injectMocks(this);
    expect(mBuilder.plugin(anyObject(EasyPowerMockPlugin.class))).andReturn(mBuilder);
    expect(mBuilder.outerRule(anyObject(PowerMockRule.class))).andReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    replay(mBuilder);
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder);
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
