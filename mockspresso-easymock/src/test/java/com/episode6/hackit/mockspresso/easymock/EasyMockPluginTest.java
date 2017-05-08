package com.episode6.hackit.mockspresso.easymock;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.easymock.EasyMock.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyMockPlugin}
 */
@RunWith(JUnit4.class)
public class EasyMockPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final EasyMockPlugin mPlugin = EasyMockPlugin.getInstance();

  @Before
  public void setup() {
    EasyMockSupport.injectMocks(this);
    expect(mBuilder.mocker(anyObject(MockerConfig.class))).andReturn(mBuilder);
  }

  @Test
  public void testConfigInstalled() {
    replay(mBuilder);
    Mockspresso.Builder returnedBuilder = mPlugin.apply(mBuilder);

    verify(mBuilder);
    assertThat(returnedBuilder)
        .isNotNull()
        .isEqualTo(mBuilder);
  }
}
