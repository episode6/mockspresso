package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.ListenableFuture;

/**
 *
 */
public class GuavaMockspressoPlugin implements MockspressoPlugin {
  @Override
  public Mockspresso.Builder apply(Mockspresso.Builder builder) {
    assert Supplier.class != null;
    assert ListenableFuture.class != null;
    return builder;
  }
}
