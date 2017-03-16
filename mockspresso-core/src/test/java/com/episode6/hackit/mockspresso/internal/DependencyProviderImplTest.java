package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.DefaultTestRunner;
import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 *
 */
@RunWith(DefaultTestRunner.class)
public class DependencyProviderImplTest {

  DependencyKey<String> mKey = new DependencyKey<>(TypeToken.of(String.class), null);

  @Mock MockerConfig.MockMaker mMockMaker;
  @Mock DependencyMap mDependencyMap;
  @Mock SpecialObjectMaker mSpecialObjectMaker;
  @Mock RealObjectMapping mRealObjectMapping;
  @Mock RealObjectMaker mRealObjectMaker;

  private DependencyProvider mDependencyProvider;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mDependencyProvider = new DependencyProviderImpl(
        mMockMaker,
        mDependencyMap,
        mSpecialObjectMaker,
        mRealObjectMapping,
        mRealObjectMaker);
  }

  @Test
  public void testDependencyMapHasKey() {
    when(mDependencyMap.containsKey(mKey)).thenReturn(true);
    when(mDependencyMap.get(mKey)).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mDependencyMap).get(mKey);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testRealObjectMapped() {
    when(mRealObjectMapping.containsKey(mKey)).thenReturn(true);
    when(mRealObjectMapping.getImplementation(mKey)).thenReturn(mKey.typeToken);
    when(mRealObjectMapping.shouldMapDependency(mKey)).thenReturn(true);
    when(mRealObjectMaker.createObject(mDependencyProvider, mKey.typeToken)).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).getImplementation(mKey);
    inOrder.verify(mRealObjectMaker).createObject(mDependencyProvider, mKey.typeToken);
    inOrder.verify(mRealObjectMapping).shouldMapDependency(mKey);
    inOrder.verify(mDependencyMap).put(mKey, "hello");
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testRealObjectNotMapped() {
    when(mRealObjectMapping.containsKey(mKey)).thenReturn(true);
    when(mRealObjectMapping.getImplementation(mKey)).thenReturn(mKey.typeToken);
    when(mRealObjectMapping.shouldMapDependency(mKey)).thenReturn(false);
    when(mRealObjectMaker.createObject(mDependencyProvider, mKey.typeToken)).thenReturn("hello");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("hello");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).getImplementation(mKey);
    inOrder.verify(mRealObjectMaker).createObject(mDependencyProvider, mKey.typeToken);
    inOrder.verify(mRealObjectMapping).shouldMapDependency(mKey);
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void testSpecialObjectSupportKey() {
    when(mSpecialObjectMaker.canMakeObject(mKey)).thenReturn(true);
    when(mSpecialObjectMaker.makeObject(mDependencyProvider, mKey)).thenReturn("testing");

    String result = mDependencyProvider.get(mKey);

    assertThat(result).isEqualTo("testing");
    InOrder inOrder = Mockito.inOrder(mMockMaker, mDependencyMap, mSpecialObjectMaker, mRealObjectMapping, mRealObjectMaker);
    inOrder.verify(mDependencyMap).containsKey(mKey);
    inOrder.verify(mRealObjectMapping).containsKey(mKey);
    inOrder.verify(mSpecialObjectMaker).canMakeObject(mKey);
    inOrder.verify(mSpecialObjectMaker).makeObject(mDependencyProvider, mKey);
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
