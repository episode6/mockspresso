package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

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
  private final RealObjectMapping mRealObjectMapping;
  private final RealObjectMaker mRealObjectMaker;

  public DependencyProviderImpl(
      MockerConfig.MockMaker mockMaker,
      DependencyMap dependencyMap,
      SpecialObjectMaker specialObjectMaker,
      RealObjectMapping realObjectMapping,
      RealObjectMaker realObjectMaker) {
    mMockMaker = mockMaker;
    mDependencyMap = dependencyMap;
    mSpecialObjectMaker = specialObjectMaker;
    mRealObjectMapping = realObjectMapping;
    mRealObjectMaker = realObjectMaker;
  }

  @Override
  public <T> T get(DependencyKey<T> key) {
    if (mDependencyMap.containsKey(key)) {
      return mDependencyMap.get(key);
    }
    if (mRealObjectMapping.containsKey(key)) {
      TypeToken<? extends T> implementationToken = mRealObjectMapping.getImplementation(key);
      T obj = mRealObjectMaker.createObject(this, implementationToken);
      if (mRealObjectMapping.shouldMapDependency(key)) {
        mDependencyMap.put(key, obj);
      }
      return obj;
    }
    if (mSpecialObjectMaker.canMakeObject(key)) {
      return mSpecialObjectMaker.makeObject(this, key);
    }
    return mMockMaker.makeMock(key.typeToken);
  }
}
