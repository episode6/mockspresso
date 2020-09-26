package com.episode6.hackit.mockspresso.easymock.powermock;

import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.easymock.annotation.MockNice;
import org.powermock.api.easymock.annotation.MockStrict;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyPowerMockFieldPreparer}
 */
@RunWith(JUnit4.class)
public class EasyPowerMockFieldPreparerTest {

  private EasyPowerMockMockerConfig mFieldPreparer;

  @Mock Runnable mMockRunnable;
  @org.powermock.api.easymock.annotation.Mock Runnable mMockRunnable2;
  @MockStrict Runnable mStrictRunnable;
  @MockNice Runnable mNiceRunnable;

  @Before
  public void setup() {
    mFieldPreparer = new EasyPowerMockMockerConfig();
  }

  @Test
  public void testMocksOnThisClass() {
    mFieldPreparer.prepareFields(this);

    assertThat(mMockRunnable)
        .isNotNull()
        .is(Conditions.mockCondition());
    assertThat(mMockRunnable2)
        .isNotNull()
        .is(Conditions.mockCondition());
    assertThat(mStrictRunnable)
        .isNotNull()
        .is(Conditions.mockCondition());
    assertThat(mNiceRunnable)
        .isNotNull()
        .is(Conditions.mockCondition());
  }
}
