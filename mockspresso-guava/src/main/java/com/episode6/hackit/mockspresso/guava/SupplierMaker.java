package com.episode6.hackit.mockspresso.guava;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Special object handling for guava's {@link Supplier}
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `automaticSuppliers()` and its
 * JavaSupport counterpart {@link MockspressoGuavaPluginsJavaSupport#automaticSuppliers()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class SupplierMaker implements SpecialObjectMaker {
  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    return key.typeToken.getRawType() == Supplier.class &&
        key.typeToken.getType() instanceof ParameterizedType;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T makeObject(
      DependencyProvider dependencyProvider, DependencyKey<T> key) {
    if (!canMakeObject(key)) {
      return null;
    }

    Type paramType = ((ParameterizedType)key.typeToken.getType()).getActualTypeArguments()[0];
    DependencyKey<?> subKey = DependencyKey.of(TypeToken.of(paramType), key.identityAnnotation);
    Object actualDependency = dependencyProvider.get(subKey);
    return (T) Suppliers.ofInstance(actualDependency);
  }
}
