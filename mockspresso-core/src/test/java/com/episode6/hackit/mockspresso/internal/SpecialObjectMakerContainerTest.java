package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Tests {@link SpecialObjectMakerContainerTest}
 */
@RunWith(DefaultTestRunner.class)
public class SpecialObjectMakerContainerTest {

  @Mock SpecialObjectMaker parentMaker;

  @Mock SpecialObjectMaker specialObjectMaker1;
  @Mock SpecialObjectMaker specialObjectMaker2;
  @Mock SpecialObjectMaker specialObjectMaker3;

  @Mock DependencyKey<String> dependencyKey;
  @Mock DependencyProvider dependencyProvider;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCanMakeWithNullParent() {
    SpecialObjectMakerContainer specialObjectMakerContainer = new SpecialObjectMakerContainer();
    specialObjectMakerContainer.add(specialObjectMaker1);
    specialObjectMakerContainer.add(specialObjectMaker2);
    specialObjectMakerContainer.add(specialObjectMaker3);

    boolean result = specialObjectMakerContainer.canMakeObject(dependencyKey);
    InOrder inOrder = inOrder(specialObjectMaker1, specialObjectMaker2, specialObjectMaker3);
    inOrder.verify(specialObjectMaker1).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker2).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker3).canMakeObject(dependencyKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(result).isFalse();
  }

  @Test
  public void testCanMakeWithNonNullParent() {
    SpecialObjectMakerContainer specialObjectMakerContainer = new SpecialObjectMakerContainer();
    specialObjectMakerContainer.setParentMaker(parentMaker);
    specialObjectMakerContainer.add(specialObjectMaker1);
    specialObjectMakerContainer.add(specialObjectMaker2);
    specialObjectMakerContainer.add(specialObjectMaker3);

    boolean result = specialObjectMakerContainer.canMakeObject(dependencyKey);
    InOrder inOrder = inOrder(specialObjectMaker1, specialObjectMaker2, specialObjectMaker3, parentMaker);
    inOrder.verify(specialObjectMaker1).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker2).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker3).canMakeObject(dependencyKey);
    inOrder.verify(parentMaker).canMakeObject(dependencyKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(result).isFalse();
  }

  @Test
  public void testMakeObjectInMaker() {
    when(specialObjectMaker2.canMakeObject(dependencyKey)).thenReturn(true);
    when(specialObjectMaker2.makeObject(dependencyProvider, dependencyKey)).thenReturn("testing");
    SpecialObjectMakerContainer specialObjectMakerContainer = new SpecialObjectMakerContainer();
    specialObjectMakerContainer.setParentMaker(parentMaker);
    specialObjectMakerContainer.add(specialObjectMaker1);
    specialObjectMakerContainer.add(specialObjectMaker2);
    specialObjectMakerContainer.add(specialObjectMaker3);

    String result = specialObjectMakerContainer.makeObject(dependencyProvider, dependencyKey);
    InOrder inOrder = inOrder(specialObjectMaker1, specialObjectMaker2, specialObjectMaker3, parentMaker);
    inOrder.verify(specialObjectMaker1).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker2).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker2).makeObject(dependencyProvider, dependencyKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(result).isEqualTo("testing");
  }

  @Test
  public void testMakeObjectInParent() {
    when(parentMaker.canMakeObject(dependencyKey)).thenReturn(true);
    when(parentMaker.makeObject(dependencyProvider, dependencyKey)).thenReturn("testing");
    SpecialObjectMakerContainer specialObjectMakerContainer = new SpecialObjectMakerContainer();
    specialObjectMakerContainer.setParentMaker(parentMaker);
    specialObjectMakerContainer.add(specialObjectMaker1);
    specialObjectMakerContainer.add(specialObjectMaker2);
    specialObjectMakerContainer.add(specialObjectMaker3);

    String result = specialObjectMakerContainer.makeObject(dependencyProvider, dependencyKey);
    InOrder inOrder = inOrder(specialObjectMaker1, specialObjectMaker2, specialObjectMaker3, parentMaker);
    inOrder.verify(specialObjectMaker1).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker2).canMakeObject(dependencyKey);
    inOrder.verify(specialObjectMaker3).canMakeObject(dependencyKey);
    inOrder.verify(parentMaker).makeObject(dependencyProvider, dependencyKey);
    inOrder.verifyNoMoreInteractions();
    assertThat(result).isEqualTo("testing");
  }
}
