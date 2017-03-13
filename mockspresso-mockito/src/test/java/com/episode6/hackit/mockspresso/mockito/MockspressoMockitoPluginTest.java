package com.episode6.hackit.mockspresso.mockito;

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
 * Tests {@link MockspressoMockitoPlugin}
 */
@RunWith(JUnit4.class)
public class MockspressoMockitoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final MockspressoMockitoPlugin mPlugin = MockspressoMockitoPlugin.getInstance();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.mockerConfig(any(MockerConfig.class))).thenReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).mockerConfig(MockspressoMockitoConfig.getInstance());
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
