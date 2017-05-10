package com.episode6.hackit.mockspresso.mockito.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link PowerMockitoPlugin}
 */
@RunWith(JUnit4.class)
public class PowerMockitoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final PowerMockitoPlugin mPlugin = new PowerMockitoPlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.mocker(any(MockerConfig.class))).thenReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).mocker(any(PowerMockitoConfig.class));
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
