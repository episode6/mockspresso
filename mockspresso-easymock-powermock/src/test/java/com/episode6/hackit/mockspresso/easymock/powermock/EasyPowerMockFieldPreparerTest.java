package com.episode6.hackit.mockspresso.easymock.powermock;

import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyPowerMockFieldPreparer}
 */
@RunWith(JUnit4.class)
public class EasyPowerMockFieldPreparerTest {

  private EasyPowerMockFieldPreparer mFieldPreparer;

  @Mock Runnable mMockRunnable;

  @Before
  public void setup() {
    mFieldPreparer = new EasyPowerMockFieldPreparer();
  }

  @Test
  public void testMocksOnThisClass() {
    mFieldPreparer.prepareFields(this);

    assertThat(mMockRunnable)
        .isNotNull()
        .is(Conditions.mockCondition());
  }
}
