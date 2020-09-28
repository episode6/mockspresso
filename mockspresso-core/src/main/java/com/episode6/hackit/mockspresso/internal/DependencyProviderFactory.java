package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.api.DependencyProvider;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory that spits out the actual {@link DependencyProvider}s which are instances
 * of a non-static inner class. We do this so each instance of our {@link DependencyProvider}
 * can own their own {@link DependencyValidator}, but we can keep that object completely
 * shielded from the external api.
 * <p>
 * The acutal {@link DependencyProvider} checks our properties in the following order
 * <p>
 * 1) DependencyMap
 * 2) RealObjectMapping / RealObjectMaker
 * 3) SpecialObjectMaker
 * 4) MockMaker
 */
class DependencyProviderFactory {

  private final MockerConfig.MockMaker mMockMaker;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMaker mSpecialObjectMaker;
  private final RealObjectMapping mRealObjectMapping;
  private final RealObjectMaker mRealObjectMaker;

  DependencyProviderFactory(
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

  DependencyProvider getDependencyProviderFor(DependencyKey topLevelKey) {
    return new DependencyProviderImpl(new DependencyValidator(topLevelKey));
  }

  DependencyProvider getBlankDependencyProvider() {
    return new DependencyProviderImpl(null);
  }

  private class DependencyProviderImpl implements DependencyProvider {

    private final @Nullable DependencyValidator mDependencyValidator;

    private DependencyProviderImpl(@Nullable DependencyValidator dependencyValidator) {
      mDependencyValidator = dependencyValidator;
    }

    @Override
    public <T> T get(@NotNull DependencyKey<T> key) {
      DependencyValidator childValidator = DependencyValidator.childOrNew(mDependencyValidator, key);
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
        @SuppressWarnings("unchecked") T specialObject = (T) mSpecialObjectMaker.makeObject(childProvider, key);
        return specialObject;
      }
      return mMockMaker.makeMock(key.typeToken);
    }
  }
}
