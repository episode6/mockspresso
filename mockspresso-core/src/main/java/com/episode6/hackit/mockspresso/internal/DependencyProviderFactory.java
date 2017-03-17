package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;

import javax.annotation.Nullable;

/**
 * Implementation of dependency provider.
 * First checks the dependency map for an explicit dep
 * Second checks the special object maker(s) to see if key is supported
 * Third returns a mock
 */
public class DependencyProviderFactory {

  private final MockerConfig.MockMaker mMockMaker;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;
  private final RealObjectMapping mRealObjectMapping;
  private final RealObjectMaker mRealObjectMaker;

  public DependencyProviderFactory(
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

  public DependencyProvider getDependencyProviderFor(DependencyKey topLevelKey) {
    return new DependencyProviderImpl(DependencyValidator.validatorFor(topLevelKey));
  }

  public DependencyProvider getBlankDependencyProvider() {
    return new DependencyProviderImpl(null);
  }

  private class DependencyProviderImpl implements DependencyProvider {

    private final @Nullable DependencyValidator mDependencyValidator;

    private DependencyProviderImpl(@Nullable DependencyValidator dependencyValidator) {
      mDependencyValidator = dependencyValidator;
    }

    @Override
    public <T> T get(DependencyKey<T> key) {
      DependencyValidator childValidator = mDependencyValidator == null ?
          DependencyValidator.validatorFor(key) :
          mDependencyValidator.child(key);
      DependencyProvider childProvider = new DependencyProviderImpl(childValidator);

      if (mDependencyMap.containsKey(key)) {
        return mDependencyMap.get(key, childValidator);
      }
      if (mRealObjectMapping.containsKey(key)) {
        TypeToken<? extends T> implementationToken = mRealObjectMapping.getImplementation(key);
        T obj = mRealObjectMaker.createObject(childProvider, implementationToken);
        if (mRealObjectMapping.shouldMapDependency(key)) {
          mDependencyMap.put(key, obj, childValidator);
        }
        return obj;
      }
      if (mSpecialObjectMaker.canMakeObject(key)) {
        return mSpecialObjectMaker.makeObject(childProvider, key);
      }
      return mMockMaker.makeMock(key.typeToken);
    }
  }
}
