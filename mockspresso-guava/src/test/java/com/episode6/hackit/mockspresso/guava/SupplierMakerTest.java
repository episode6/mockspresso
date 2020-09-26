package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.google.common.base.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests {@link SupplierMaker}
 */
public class SupplierMakerTest {

  static final TypeToken<Supplier<String>> typeToken = new TypeToken<Supplier<String>>() {};
  static final DependencyKey<Supplier<String>> key = DependencyKey.of(typeToken);

  @Mock DependencyProvider mDependencyProvider;

  final SupplierMaker mSupplierMaker = new SupplierMaker();

  AutoCloseable mockitoClosable;

  @After public void teardown() throws Exception {
    mockitoClosable.close();
  }

  @Before public void setup() {
    mockitoClosable = MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateSupplier() {
    when(mDependencyProvider.get(DependencyKey.of(String.class))).thenReturn("hello world");

    boolean canMake = mSupplierMaker.canMakeObject(key);
    Supplier<String> supplier = mSupplierMaker.makeObject(mDependencyProvider, key);

    verify(mDependencyProvider).get(DependencyKey.of(String.class));
    assertThat(canMake).isTrue();
    assertThat(supplier.get()).isEqualTo("hello world");
  }

  @Test
  public void testCantCreateUnParameterizedSupplier() {
    boolean canMake = mSupplierMaker.canMakeObject(DependencyKey.of(Supplier.class));
    Supplier supplier = mSupplierMaker.makeObject(mDependencyProvider, DependencyKey.of(Supplier.class));

    assertThat(canMake).isFalse();
    assertThat(supplier).isNull();
    verifyNoMoreInteractions(mDependencyProvider);
  }
}
