package com.episode6.hackit.mockspresso.quick;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 *
 */
public class QuickMockspressoBuilderTest {

  @Test
  public void placeholderTest() {
    int input = 1;
    int expectedOutput = 2;

    int output = input+1;

    assertThat(output).isEqualTo(expectedOutput);
  }
}
