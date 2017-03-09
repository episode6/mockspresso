package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;

/**
 * Implementation of dependency provider.
 * First checks the dependency map for an explicit dep
 * Second checks the special object maker(s) to see if key is supported
 * Third returns a mock
 */
public class DependencyProviderImpl implements DependencyProvider {

  private final MockerConfig.MockMaker mMockMaker;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;

  public DependencyProviderImpl(
      MockerConfig.MockMaker mockMaker,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker) {
    mMockMaker = mockMaker;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
  }

  @Override
  public <T> T get(DependencyKey<T> key) {
    if (mDependencyMap.containsKey(key)) {
      return mDependencyMap.get(key);
    }
    if (mSpecialObjectMaker.canMakeObject(key)) {
      return mSpecialObjectMaker.makeObject(this, key);
    }
    return mMockMaker.makeMock(key.typeToken);
  }
}
