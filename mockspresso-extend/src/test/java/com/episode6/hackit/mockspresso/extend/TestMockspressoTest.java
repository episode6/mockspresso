package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.extend.testext.BuildTestMockspresso;
import com.episode6.hackit.mockspresso.extend.testext.TestMockspresso;
import com.episode6.hackit.mockspresso.extend.testobj.TestMockObject;
import com.episode6.hackit.mockspresso.extend.testobj.TestRealObject;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class TestMockspressoTest {

  @Rule
  public final TestMockspresso.Rule mockspresso = BuildTestMockspresso.with()
      .simpleInjector()
      .mockWithMockito()
      .buildRule();

  @Mock TestMockObject mockObject;
  @RealObject final String name = "some_name";
  @RealObject TestRealObject testRealObject;

  @Test
  public void testBasicUsage() {
    testRealObject.doThing();

    verify(mockObject).doSomething();

    assertThat(testRealObject.getName()).isEqualTo("some_name");
  }

  @Test
  public void testBuildUpon() {
    TestRealObject newTestObject = mockspresso.buildUpon()
        .simpleInjector()
        .mockWithMockito()
        .dependency(String.class, "some_other_name")
        .build()
        .create(TestRealObject.class);
    newTestObject.doThing();

    verify(mockObject).doSomething();

    assertThat(newTestObject.getName()).isEqualTo("some_other_name");
  }
}
