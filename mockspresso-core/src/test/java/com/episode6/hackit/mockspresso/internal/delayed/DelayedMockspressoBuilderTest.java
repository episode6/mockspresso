package com.episode6.hackit.mockspresso.internal.delayed;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.internal.MockspressoConfigContainer;
import com.episode6.hackit.mockspresso.mockito.MockitoMockerConfig;
import com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.episode6.hackit.mockspresso.Conditions.rawClass;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link DelayedMockspressoBuilder}
 */
@RunWith(DefaultTestRunner.class)
public class DelayedMockspressoBuilderTest {

  private final DelayedMockspressoBuilder mDelayedBuilder = new DelayedMockspressoBuilder();

  @Test
  public void testCanBuildWithNothing() {
    Mockspresso mockspresso = mDelayedBuilder.build();

    assertThat(mockspresso)
        .isNotNull();
  }

  @Test
  public void testCanCreateWithParent() {
    MockspressoConfigContainer configContainer = mock(MockspressoConfigContainer.class);
    when(configContainer.getMockerConfig()).thenReturn(MockitoMockerConfig.getInstance());
    when(configContainer.getInjectionConfig()).thenReturn(SimpleInjectionConfig.getInstance());

    Mockspresso mockspresso = mDelayedBuilder.build();
    mDelayedBuilder.setParent(configContainer);
    TestClass testObject = mockspresso.create(TestClass.class);

    assertThat(testObject)
        .isNotNull()
        .is(rawClass(TestClass.class));
  }

  @Test(expected = NullPointerException.class)
  public void testCreateFailsWithoutParent() {
    Mockspresso mockspresso = mDelayedBuilder.build();
    mockspresso.create(TestClass.class);
  }

  @Test(expected = VerifyError.class)
  public void testFailOnBuildRule() {
    mDelayedBuilder.buildRule();
  }

  public static class TestClass {}
}
