package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Special object maker for guava's {@link ListenableFuture}. Uses param type
 * and ident annotation to look up actual dependency, and returns an immediate
 * future.
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `automaticListenableFutures()` and its
 * JavaSupport counterpart {@link MockspressoGuavaPluginsJavaSupport#automaticListenableFutures()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class ListenableFutureMaker implements SpecialObjectMaker {
  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    return key.typeToken.getRawType() == ListenableFuture.class &&
        key.typeToken.getType() instanceof ParameterizedType;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeObject(DependencyProvider dependencyProvider, DependencyKey<T> key) {
    if (!canMakeObject(key)) {
      return null;
    }

    Type paramType = ((ParameterizedType)key.typeToken.getType()).getActualTypeArguments()[0];
    DependencyKey<?> subKey = DependencyKey.of(TypeToken.of(paramType), key.identityAnnotation);
    Object actualDependency = dependencyProvider.get(subKey);
    return (T) Futures.immediateFuture(actualDependency);
  }
}
