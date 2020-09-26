package com.episode6.hackit.mockspresso.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Sanity checks for {@link MockitoMockerConfig}
 */
@RunWith(JUnit4.class)
public class MockitoMockerConfigTest {

  private final MockitoMockerConfig mMockitoConfig = new MockitoMockerConfig();

  @SuppressWarnings("unchecked")
  @Test
  public void testIncludedAnnotations() {
    assertThat(mMockitoConfig.provideMockAnnotations())
        .contains(
            Spy.class,
            Mock.class);
  }
}
