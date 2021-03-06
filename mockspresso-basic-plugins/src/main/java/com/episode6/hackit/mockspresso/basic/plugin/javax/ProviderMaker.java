package com.episode6.hackit.mockspresso.basic.plugin.javax;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.inject.Provider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * An implementation of SpecialObjectMaker for {@link javax.inject.Provider}.
 *
 * @deprecated This functionality is now exposed by the kotlin extension method `automaticProviders()` and its
 * JavaSupport counterpart
 * {@link com.episode6.hackit.mockspresso.basic.plugin.MockspressoBasicPluginsJavaSupport#automaticProviders()}
 *
 * This class will be marked internal/protected in a future release
 */
@Deprecated
public class ProviderMaker implements SpecialObjectMaker {

  @Override
  public boolean canMakeObject(DependencyKey<?> key) {
    return key.typeToken.getRawType() == Provider.class &&
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
    return (T) new DependencyInstanceProvider(dependencyProvider, subKey);
  }

  private static class DependencyInstanceProvider<V> implements Provider<V> {
    private final DependencyProvider mDependencyProvider;
    private final DependencyKey<V> mDependencyKey;

    private DependencyInstanceProvider(
        DependencyProvider dependencyProvider,
        DependencyKey<V> dependencyKey) {
      mDependencyProvider = dependencyProvider;
      mDependencyKey = dependencyKey;
    }

    @Override
    public V get() {
      return mDependencyProvider.get(mDependencyKey);
    }
  }
}
