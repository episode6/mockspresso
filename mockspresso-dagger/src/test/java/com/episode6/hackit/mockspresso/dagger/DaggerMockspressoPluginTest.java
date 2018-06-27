package com.episode6.hackit.mockspresso.dagger;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.basic.plugin.javax.JavaxInjectMockspressoPlugin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link DaggerMockspressoPlugin}
 */
public class DaggerMockspressoPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final DaggerMockspressoPlugin mPlugin = new DaggerMockspressoPlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.plugin(any(JavaxInjectMockspressoPlugin.class))).thenReturn(mBuilder);
    when(mBuilder.specialObjectMaker(any(SpecialObjectMaker.class))).thenReturn(mBuilder);
  }

  @Test
  public void testInjectionConfigSet() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).plugin(any(JavaxInjectMockspressoPlugin.class));
    verify(mBuilder).specialObjectMaker(any(DaggerLazyMaker.class));
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
