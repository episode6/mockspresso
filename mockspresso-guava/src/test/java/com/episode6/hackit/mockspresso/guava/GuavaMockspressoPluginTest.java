package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link GuavaMockspressoPlugin}
 */
public class GuavaMockspressoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final GuavaMockspressoPlugin mPlugin = new GuavaMockspressoPlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void placeholderTest() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
