package com.episode6.hackit.mockspresso.plugin.javax;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link JavaxInjectMockspressoPlugin}
 */
@RunWith(DefaultTestRunner.class)
public class JavaxInjectMockspressoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final JavaxInjectMockspressoPlugin mPlugin = JavaxInjectMockspressoPlugin.getInstance();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.injector(any(InjectionConfig.class))).thenReturn(mBuilder);
    when(mBuilder.specialObjectMaker(any(SpecialObjectMaker.class))).thenReturn(mBuilder);
  }

  @Test
  public void testInjectionConfigSet() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).injector(JavaxInjectionConfig.getInstance());
    verify(mBuilder).specialObjectMaker(ProviderMaker.getInstance());
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
