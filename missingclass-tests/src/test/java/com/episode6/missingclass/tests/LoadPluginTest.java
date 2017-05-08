package com.episode6.missingclass.tests;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.exception.MissingDependencyError;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests loading of plugins w/o having the appropriate dependencies
 */
public class LoadPluginTest {

  @Test
  public void testMockitoLoad() {
    doTest(
        "org.mockito:mockito-core v2.x",
        new Runnable() {
          @Override
          public void run() {
            BuildMockspresso.with().mocker().mockito();
          }
        });
  }

  @Test
  public void testEasyMockLoad() {
    doTest(
        "org.easymock:easymock v3.4",
        new Runnable() {
          @Override
          public void run() {
            BuildMockspresso.with().mocker().easyMock();
          }
        });
  }

  private void doTest(String expectedDependency, Runnable runnable) {
    try {
      runnable.run();
    } catch (MissingDependencyError e) {
      assertThat(e.getMessage())
          .startsWith("Missing Dependency: ")
          .endsWith(expectedDependency);
    }
  }
}
