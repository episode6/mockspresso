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
 * Tests {@link MockitoPlugin}
 */
@RunWith(JUnit4.class)
public class MockitoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final MockitoPlugin mPlugin = new MockitoPlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.mocker(any(MockerConfig.class))).thenReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).mocker(any(MockitoMockerConfig.class));
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
