package com.episode6.hackit.mockspresso.mockito.powermock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Spy;

import static com.episode6.hackit.mockspresso.mockito.powermock.Conditions.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link PowerMockitoFieldPreparer}
 */
@RunWith(JUnit4.class)
public class PowerMockitoFieldPreparerTest {

  private PowerMockitoConfig mFieldPreparer;

  @Mock Runnable mMockRunnable;
  @Spy Runnable mSpyRunnable = new Runnable() {
    @Override
    public void run() {

    }
  };
  @Spy Runnable mSpyRunnable2;

  @Before
  public void setup() {
    mFieldPreparer = new PowerMockitoConfig();
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
