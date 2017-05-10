package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.rule.PowerMockRule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link PowerMockitoRulePlugin}
 */
@RunWith(JUnit4.class)
public class PowerMockitoRulePluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final PowerMockitoRulePlugin mPlugin = new PowerMockitoRulePlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.plugin(any(MockspressoPlugin.class))).thenReturn(mBuilder);
    when(mBuilder.outerRule(any(MethodRule.class))).thenReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).plugin(any(PowerMockitoPlugin.class));
    verify(mBuilder).outerRule(any(PowerMockRule.class));
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
