package com.episode6.hackit.mockspresso.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

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

  @Before
  public void setup() {
    mFieldPreparer = new MockitoFieldPreparer();
  }

  @Test
  public void testMocksOnThisClass() {
    mFieldPreparer.prepareFields(this);

    assertThat(mMockRunnable).isNotNull();
    assertThat(Mockito.mockingDetails(mMockRunnable).isMock()).isTrue();
    assertThat(mSpyRunnable).isNotNull();
    assertThat(Mockito.mockingDetails(mSpyRunnable).isSpy()).isTrue();
  }

}
