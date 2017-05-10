package com.episode6.hackit.mockspresso.mockito.powermock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Sanity checks for {@link PowerMockitoConfig}
 */
@RunWith(JUnit4.class)
public class PowerMockitoConfigTest {

  private final PowerMockitoConfig mMockitoConfig = new PowerMockitoConfig();

  @SuppressWarnings("unchecked")
  @Test
  public void testIncludedAnnotations() {
    assertThat(mMockitoConfig.provideMockAnnotations())
        .contains(
            Spy.class,
            Mock.class);
  }

  @Test
  public void testMockMakerIsExpected() {
    assertThat(mMockitoConfig.provideMockMaker())
        .isNotNull()
        .isInstanceOf(PowerMockitoMockMaker.class);
  }

  @Test
  public void testFieldPreparerIsExpected() {
    assertThat(mMockitoConfig.provideFieldPreparer())
        .isNotNull()
        .isInstanceOf(PowerMockitoFieldPreparer.class);
  }
}
