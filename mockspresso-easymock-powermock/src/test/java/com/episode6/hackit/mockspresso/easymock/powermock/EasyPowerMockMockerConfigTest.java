package com.episode6.hackit.mockspresso.easymock.powermock;

import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.api.easymock.annotation.MockStrict;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Sanity checks for {@link EasyPowerMockMockerConfig}
 */
@RunWith(JUnit4.class)
public class EasyPowerMockMockerConfigTest {

  private final EasyPowerMockMockerConfig mEasyMockConfig = new EasyPowerMockMockerConfig();

  @SuppressWarnings("unchecked")
  @Test
  public void testIncludedAnnotations() {
    assertThat(mEasyMockConfig.provideMockAnnotations())
        .contains(
            Mock.class,
            org.powermock.api.easymock.annotation.Mock.class,
            MockNice.class,
            MockStrict.class);
  }

  @Test
  public void testMockMakerIsExpected() {
    assertThat(mEasyMockConfig.provideMockMaker())
        .isNotNull()
        .isInstanceOf(EasyPowerMockMockMaker.class);
  }

  @Test
  public void testFieldPreparerIsExpected() {
    assertThat(mEasyMockConfig.provideFieldPreparer())
        .isNotNull()
        .isInstanceOf(EasyPowerMockFieldPreparer.class);
  }
}
