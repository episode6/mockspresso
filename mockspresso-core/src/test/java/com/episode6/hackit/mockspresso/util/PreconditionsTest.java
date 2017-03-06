package com.episode6.hackit.mockspresso.util;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}
