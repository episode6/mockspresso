package com.episode6.hackit.mockspresso.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Sanity checks for {@link MockspressoMockitoConfig}
 */
@RunWith(JUnit4.class)
public class MockspressoMockitoConfigTest {

  private final MockspressoMockitoConfig mMockitoConfig = MockspressoMockitoConfig.getInstance();

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
        .isInstanceOf(MockitoMockMaker.class);
  }

  @Test
  public void testFieldPreparerIsExpected() {
    assertThat(mMockitoConfig.provideFieldPreparer())
        .isNotNull()
        .isInstanceOf(MockitoFieldPreparer.class);
  }
}
