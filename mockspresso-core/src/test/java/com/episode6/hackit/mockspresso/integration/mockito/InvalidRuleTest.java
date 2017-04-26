package com.episode6.hackit.mockspresso.integration.mockito;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.mockito.MockitoPlugin;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 *
 */
@RunWith(DefaultTestRunner.class)
public class InvalidRuleTest {

  private final TestClass mInitializerWithFields = spy(new TestClass());

  public final Mockspresso.Rule invalidMockspresso = Mockspresso.Builders.simple()
      .plugin(MockitoPlugin.getInstance())
      .testResources(mInitializerWithFields)
      .buildRule();

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
    verifyZeroInteractions(mInitializerWithFields);
  }

  @Test
  public void testObjectFieldsNotSet() {
    assertThat(mInitializerWithFields.mRunnable).isNull();
  }

  public static class TestClass {
    @Mock Runnable mRunnable;

    @Before
    public void setup(Mockspresso mockspresso) {

    }
  }
}
