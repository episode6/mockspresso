package com.episode6.hackit.mockspresso.mockito.powermock.integration.rule;

import com.episode6.hackit.mockspresso.BuildMockspresso;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport.injectBySimpleConfig;
import static com.episode6.hackit.mockspresso.mockito.powermock.MockspressoPowerMockitoPluginsJavaSupport.mockByPowerMockitoRule;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests how a mockspresso rule acts when not properly annotated (i.e. never applied)
 */
public class InvalidRuleTestPowerMockitoRule {

  private final TestClass mInitializerWithFields = new TestClass();

  public final Mockspresso.Rule invalidMockspresso = BuildMockspresso.with()
      .plugin(injectBySimpleConfig())
      .plugin(mockByPowerMockitoRule())
      .testResources(mInitializerWithFields)
      .buildRule();

  Notifier notifier;

  @Before
  public void setup() {
    notifier = mock(Notifier.class);
  }

  @Test(expected = NullPointerException.class)
  public void testCantCreateClass() {
    invalidMockspresso.create(String.class);
  }

  @Test(expected = NullPointerException.class)
  public void testCantCreateToken() {
    invalidMockspresso.create(TypeToken.of(String.class));
  }

  @Test(expected = NullPointerException.class)
  public void testCantCreateClassFromChild() {
    invalidMockspresso.buildUpon().build().create(String.class);
  }

  @Test(expected = NullPointerException.class)
  public void testCantCreateTokenFromChild() {
    invalidMockspresso.buildUpon().build().create(TypeToken.of(String.class));
  }

  @Test
  public void testInitializerNotRun() {
    verifyZeroInteractions(notifier);
  }

  @Test
  public void testObjectFieldsNotSet() {
    assertThat(mInitializerWithFields.mRunnable).isNull();
  }

  interface Notifier {
    void setup(Mockspresso mockspresso);
  }

  public class TestClass {
    @Mock Runnable mRunnable;

    @Before
    public void setup(Mockspresso mockspresso) {
      notifier.setup(mockspresso);
    }
  }
}
