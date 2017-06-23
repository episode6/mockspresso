package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests {@link ListenableFutureMaker}
 */
public class ListenableFutureMakerTest {

  static final TypeToken<ListenableFuture<String>> typeToken = new TypeToken<ListenableFuture<String>>() {};
  static final DependencyKey<ListenableFuture<String>> key = DependencyKey.of(typeToken);

  @Mock DependencyProvider mDependencyProvider;

  final ListenableFutureMaker mFutureMaker = new ListenableFutureMaker();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreteFuture() throws ExecutionException, InterruptedException {
    when(mDependencyProvider.get(DependencyKey.of(String.class))).thenReturn("hi there");

    boolean canMake = mFutureMaker.canMakeObject(key);
    ListenableFuture<String> future = mFutureMaker.makeObject(mDependencyProvider, key);

    verify(mDependencyProvider).get(DependencyKey.of(String.class));
    assertThat(canMake).isTrue();
    assertThat(future).isNotNull();
    assertThat(future.get()).isEqualTo("hi there");
  }

  @Test
  public void testCantCreateGenericFuture() {
    boolean canMake = mFutureMaker.canMakeObject(DependencyKey.of(ListenableFuture.class));
    ListenableFuture future = mFutureMaker.makeObject(mDependencyProvider, DependencyKey.of(ListenableFuture.class));

    assertThat(canMake).isFalse();
    assertThat(future).isNull();
    verifyNoMoreInteractions(mDependencyProvider);
  }
}
