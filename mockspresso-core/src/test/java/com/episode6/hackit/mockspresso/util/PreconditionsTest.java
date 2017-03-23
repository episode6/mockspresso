package com.episode6.hackit.mockspresso.util;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link Preconditions}
 */
@RunWith(DefaultTestRunner.class)
public class PreconditionsTest {

  @Test
  public void testAssertNullPass() {
    String test = null;

    Preconditions.assertNull(test, "Unexpected failure");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAssertNullFail() {
    String test = "hi there";

    Preconditions.assertNull(test, "expected exception");
  }

  @Test
  public void testAssertNotNullPass() {
    String test = "hi there";

    String result = Preconditions.assertNotNull(test, "shouldnt be null");
    assertThat(result).isEqualTo(test);
  }

  @Test(expected = NullPointerException.class)
  public void testAssertNotNullFail() {
    String test = null;

    Preconditions.assertNotNull(test, "should fail.");
  }

  @Test
  public void testAssertTruePass() {
    Preconditions.assertTrue(true, "should pass");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAssertTrueFail() {
    Preconditions.assertTrue(false, "should fail");
  }
}
