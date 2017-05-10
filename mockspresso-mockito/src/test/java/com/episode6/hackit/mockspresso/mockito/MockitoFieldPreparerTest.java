package com.episode6.hackit.mockspresso.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Spy;

import static com.episode6.hackit.mockspresso.mockito.Conditions.mockCondition;
import static com.episode6.hackit.mockspresso.mockito.Conditions.spyCondition;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link MockitoFieldPreparer}
 */
@RunWith(JUnit4.class)
public class MockitoFieldPreparerTest {

  private MockitoFieldPreparer mFieldPreparer;

  @Mock Runnable mMockRunnable;
  @Spy Runnable mSpyRunnable = new Runnable() {
    @Override
    public void run() {

    }
  };
  @Spy Runnable mSpyRunnable2;

  @Before
  public void setup() {
    mFieldPreparer = new MockitoFieldPreparer();
  }

  @Test
  public void testMocksOnThisClass() {
    mFieldPreparer.prepareFields(this);

    assertThat(mMockRunnable)
        .isNotNull()
        .is(mockCondition());
    assertThat(mSpyRunnable)
        .isNotNull()
        .is(spyCondition());
    assertThat(mSpyRunnable2)
        .isNotNull()
        .is(spyCondition());
  }

}
