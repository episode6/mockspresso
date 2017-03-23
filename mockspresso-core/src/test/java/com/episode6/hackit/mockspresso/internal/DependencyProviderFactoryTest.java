package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Tests {@link DependencyProviderFactory}
 */
@RunWith(DefaultTestRunner.class)
public class DependencyProviderFactoryTest {

  DependencyKey<String> mKey = DependencyKey.of(String.class);

  @Mock MockerConfig.MockMaker mMockMaker;
  @Mock DependencyMap mDependencyMap;
  @Mock SpecialObjectMaker mSpecialObjectMaker;
  @Mock RealObjectMapping mRealObjectMapping;
  @Mock RealObjectMaker mRealObjectMaker;

  private DependencyProvider mDependencyProvider;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mDependencyProvider = new DependencyProviderFactory(
        mMockMaker,
        mDependencyMap,
        mSpecialObjectMaker,
        mRealObjectMapping,
        mRealObjectMaker).getBlankDependencyProvider();
  }

  @Test
  public void testDependencyMapHasKey() {
    when(mDependencyMap.containsKey(mKey)).thenReturn(true);
    when(mDependencyMap.get(eq(mKey), any(DependencyValidator.class))).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mDependencyMap).get(eq(mKey), any(DependencyValidator.class));
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testRealObjectMapped() {
    when(mRealObjectMapping.containsKey(mKey)).thenReturn(true);
    when(mRealObjectMapping.getImplementation(mKey)).thenReturn(mKey.typeToken);
    when(mRealObjectMapping.shouldMapDependency(mKey)).thenReturn(true);
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(mKey.typeToken))).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).getImplementation(mKey);
    inOrder.verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(mKey.typeToken));
    inOrder.verify(mRealObjectMapping).shouldMapDependency(mKey);
    inOrder.verify(mDependencyMap).put(eq(mKey), eq("hello"), any(DependencyValidator.class));
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testRealObjectNotMapped() {
    when(mRealObjectMapping.containsKey(mKey)).thenReturn(true);
    when(mRealObjectMapping.getImplementation(mKey)).thenReturn(mKey.typeToken);
    when(mRealObjectMapping.shouldMapDependency(mKey)).thenReturn(false);
    when(mRealObjectMaker.createObject(any(DependencyProvider.class), eq(mKey.typeToken))).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).getImplementation(mKey);
    inOrder.verify(mRealObjectMaker).createObject(any(DependencyProvider.class), eq(mKey.typeToken));
    inOrder.verify(mRealObjectMapping).shouldMapDependency(mKey);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testSpecialObjectSupportKey() {
    when(mSpecialObjectMaker.canMakeObject(mKey)).thenReturn(true);
    when(mSpecialObjectMaker.makeObject(any(DependencyProvider.class), eq(mKey))).thenReturn("testing");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("testing");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mSpecialObjectMaker).canMakeObject(mKey);
    inOrder.verify(mSpecialObjectMaker).makeObject(any(DependencyProvider.class), eq(mKey));
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testFallbackToMockMaker() {
    when(mMockMaker.makeMock(mKey.typeToken)).thenReturn("testing again");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("testing again");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mSpecialObjectMaker).canMakeObject(mKey);
    inOrder.verify(mMockMaker).makeMock(mKey.typeToken);
    inOrder.verifyNoMoreInteractions();
  }
}
