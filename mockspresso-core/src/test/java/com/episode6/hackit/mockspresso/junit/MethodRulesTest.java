package com.episode6.hackit.mockspresso.junit;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests {@link MethodRules}
 */
@RunWith(DefaultTestRunner.class)
public class MethodRulesTest {

  @Mock TestRule mockTestRule;
  @Mock Statement mockStatement;
  @Mock FrameworkMethod mockFrameworkMethod;
  @Mock Object mockTestObject;

  @Captor ArgumentCaptor<Description> descriptionCaptor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testWrappingSimpleTestRule() {
    Statement replacementStatement = mock(Statement.class);
    when(mockTestRule.apply(any(Statement.class), any(Description.class))).thenReturn(replacementStatement);
    when(mockFrameworkMethod.getName()).thenReturn("testMethodName");

    MethodRule methodRule = MethodRules.wrapTestRule(mockTestRule);
    Statement resultStatement = methodRule.apply(mockStatement, mockFrameworkMethod, mockTestObject);

    verify(mockTestRule).apply(eq(mockStatement), descriptionCaptor.capture());
    verifyNoMoreInteractions(mockStatement);
    assertThat(descriptionCaptor.getValue().getMethodName()).isEqualTo("testMethodName");
    assertThat(resultStatement).isEqualTo(replacementStatement);
  }
}
