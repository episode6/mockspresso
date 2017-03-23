package com.episode6.hackit.mockspresso.easymock;

import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Sanity checks for {@link EasyMockMockerConfig}
 */
@RunWith(JUnit4.class)
public class EasyMockMockerConfigTest {

  private final EasyMockMockerConfig mEasyMockConfig = EasyMockMockerConfig.getInstance();

  @SuppressWarnings("unchecked")
  @Test
  public void testIncludedAnnotations() {
    assertThat(mEasyMockConfig.provideMockAnnotations())
        .contains(Mock.class);
  }

  @Test
  public void testMockMakerIsExpected() {
    assertThat(mEasyMockConfig.provideMockMaker())
        .isNotNull()
        .isInstanceOf(EasyMockMockMaker.class);
  }

  @Test
  public void testFieldPreparerIsExpected() {
    assertThat(mEasyMockConfig.provideFieldPreparer())
        .isNotNull()
        .isInstanceOf(EasyMockFieldPreparer.class);
  }
}
