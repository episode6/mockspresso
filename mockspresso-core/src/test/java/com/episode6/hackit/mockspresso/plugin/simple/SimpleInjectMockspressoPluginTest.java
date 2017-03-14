package com.episode6.hackit.mockspresso.plugin.simple;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link SimpleInjectMockspressoPlugin}
 */
@RunWith(DefaultTestRunner.class)
public class SimpleInjectMockspressoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final SimpleInjectMockspressoPlugin mPlugin = SimpleInjectMockspressoPlugin.getInstance();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.injectionConfig(any(InjectionConfig.class))).thenReturn(mBuilder);
  }

  @Test
  public void testInjectionConfigSet() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).injectionConfig(SimpleInjectionConfig.getInstance());
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
