package com.episode6.hackit.mockspresso.easymock;

import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.episode6.hackit.mockspresso.easymock.Conditions.mockCondition;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyMockFieldPreparer}
 */
@RunWith(JUnit4.class)
public class EasyMockFieldPreparerTest {

  private EasyMockMockerConfig mFieldPreparer;

  @Mock Runnable mMockRunnable;

  @Before
  public void setup() {
    mFieldPreparer = new EasyMockMockerConfig();
  }

  @Test
  public void testMocksOnThisClass() {
    mFieldPreparer.prepareFields(this);

    assertThat(mMockRunnable)
        .isNotNull()
        .is(mockCondition());
  }
}
