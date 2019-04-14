package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * @deprecated This functionality is now exposed by the kotlin extension methods
 * `automaticListenableFutures()` and `automaticSuppliers()` and their JavaSupport
 * counterparts {@link MockspressoGuavaPluginsJavaSupport#automaticListenableFutures()}
 * and {@link MockspressoGuavaPluginsJavaSupport#automaticSuppliers()}
 */
@Deprecated
public class GuavaMockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert Supplier.class != null;
    assert ListenableFuture.class != null;
    return builder
        .specialObjectMaker(new ListenableFutureMaker())
        .specialObjectMaker(new SupplierMaker());
  }
}
