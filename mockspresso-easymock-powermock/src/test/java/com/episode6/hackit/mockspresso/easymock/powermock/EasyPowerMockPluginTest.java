package com.episode6.hackit.mockspresso.easymock.powermock;

import com.episode6.hackit.mockspresso.Mockspresso;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.easymock.EasyMock.*;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests {@link EasyPowerMockPlugin}
 */
@RunWith(JUnit4.class)
public class EasyPowerMockPluginTest {

  @Mock Mockspresso.Builder mBuilder;

  private final EasyPowerMockPlugin mPlugin = new EasyPowerMockPlugin();

  @Before
  public void setup() {
    EasyMockSupport.injectMocks(this);
    expect(mBuilder.mocker(anyObject(EasyPowerMockMockerConfig.class))).andReturn(mBuilder);
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
