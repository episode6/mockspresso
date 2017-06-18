package com.episode6.hackit.mockspresso.dagger1;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@link Dagger1MockspressoPlugin}
 */
public class Dagger1MockspressoPluginTest {

  @Mock Mockspresso.Builder mBuilder;
  @Mock Mockspresso.InjectorPicker mInjectorPicker;

  private final Dagger1MockspressoPlugin mPlugin = new Dagger1MockspressoPlugin();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mBuilder.injector()).thenReturn(mInjectorPicker);
    when(mInjectorPicker.javax()).thenReturn(mBuilder);
    when(mBuilder.specialObjectMaker(any(SpecialObjectMaker.class))).thenReturn(mBuilder);
  }

  @Test
  public void testInjectionConfigSet() {
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder).injector();
    verify(mInjectorPicker).javax();
    verify(mBuilder).specialObjectMaker(any(Dagger1LazyMaker.class));
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
