package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.internal.delayed.MockspressoRuleImpl;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  private final List<Object> mObjectsWithFields = new LinkedList<>();
  private final DependencyMap mDependencyMap = new DependencyMap();
  private final SpecialObjectMakerContainer mSpecialObjectMakers = new SpecialObjectMakerContainer();
  private final RealObjectMapping mRealObjectMapping = new RealObjectMapping();

  private @Nullable MockerConfig mMockerConfig = null;
  private @Nullable InjectionConfig mInjectionConfig = null;

  public void setParent(MockspressoConfigContainer parentConfig) {
    mDependencyMap.setParentMap(parentConfig.getDependencyMap());
    mSpecialObjectMakers.setParentMaker(parentConfig.getSpecialObjectMaker());
    mRealObjectMapping.setParentMap(parentConfig.getRealObjectMapping());
    if (mMockerConfig == null) {
      mMockerConfig = parentConfig.getMockerConfig();
    }
    if (mInjectionConfig == null) {
      mInjectionConfig = parentConfig.getInjectionConfig();
    }
  }

  @Override
  public Mockspresso.Builder plugin(MockspressoPlugin plugin) {
    return plugin.apply(this);
  }

  public Mockspresso.Builder fieldsFrom(Object objectWithFields) {
    mObjectsWithFields.add(objectWithFields);
    return this;
  }

  @Override
  public Mockspresso.Builder mockerConfig(MockerConfig mockerConfig) {
    mMockerConfig = mockerConfig;
    return this;
  }

  @Override
  public Mockspresso.Builder injectionConfig(InjectionConfig injectionConfig) {
    mInjectionConfig = injectionConfig;
    return this;
  }

  @Override
  public Mockspresso.Builder specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
    mSpecialObjectMakers.add(specialObjectMaker);
    return this;
  }

  @Override
  public Mockspresso.Builder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
    mSpecialObjectMakers.addAll(specialObjectMakers);
    return this;
  }

  @Override
  public <T> Mockspresso.Builder dependency(Class<T> clazz, T value) {
    return dependency(DependencyKey.of(clazz), value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(TypeToken<T> typeToken, T value) {
    return dependency(DependencyKey.of(typeToken), value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(DependencyKey<T> key, T value) {
    mDependencyMap.put(key, value, null);
    return this;
  }

  @Override
  public <T> Mockspresso.Builder useRealObject(Class<T> objectClass) {
    return useRealObject(TypeToken.of(objectClass));
  }

  @Override
  public <T> Mockspresso.Builder useRealObject(TypeToken<T> objectToken) {
    return useRealObject(DependencyKey.of(objectToken), objectToken);
  }

  @Override
  public <T> Mockspresso.Builder useRealObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
    return useRealObject(key, TypeToken.of(implementationClass));
  }

  @Override
  public <T> Mockspresso.Builder useRealObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
    mRealObjectMapping.put(key, implementationToken, false);
    return this;
  }

  @Override
  public Mockspresso build() {
    return buildInternal();
  }

  @Override
  public Mockspresso.Rule buildRule() {
    return new MockspressoRuleImpl(buildInternal());
  }

  public MockspressoInternal buildInternal() {
    // verify
    Preconditions.assertNotNull(mMockerConfig, "MockerConfig missing from mockspresso builder");
    Preconditions.assertNotNull(mInjectionConfig, "InjectionConfig missing from mockspresso builder");

    // create the objects we use to create objects
    RealObjectMaker realObjectMaker = new RealObjectMaker(
        mInjectionConfig.provideConstructorSelector(),
        mInjectionConfig.provideInjectableFieldAnnotations(),
        mInjectionConfig.provideInjectableMethodAnnotations());
    DependencyProviderFactory dependencyProviderFactory = new DependencyProviderFactory(
        mMockerConfig.provideMockMaker(),
        mDependencyMap,
        mSpecialObjectMakers,
        mRealObjectMapping,
        realObjectMaker);

    // prepare mObjectsWithFields
    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        mRealObjectMapping);
    DependencyMapImporter mDependencyMapImporter = new DependencyMapImporter(mDependencyMap);
    MockerConfig.FieldPreparer mockFieldPreparer = mMockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mMockerConfig.provideMockAnnotations();
    for (Object o : mObjectsWithFields) {
      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      // import mocks and non-null real objects into dependency map
      mDependencyMapImporter.importAnnotatedFields(o, mockAnnotations);
      mDependencyMapImporter.importAnnotatedFields(o, RealObject.class);

      // track down any @RealObjects that are null
      realObjectFieldTracker.scanNullRealObjectFields(o);
    }

    // since we haven't built any real objects yet, assert that we haven't
    // accidentally mapped a mock or other dependency to any of our RealObject keys
    mDependencyMap.assertDoesNotContainAny(realObjectFieldTracker.keySet());

    // fetch real object values from the dependencyProvider (now that they've been mapped)
    // and apply them to the fields found in realObjectFieldTracker
    for (DependencyKey key : realObjectFieldTracker.keySet()) {
      realObjectFieldTracker.applyValueToFields(key, dependencyProviderFactory.getBlankDependencyProvider().get(key));
    }

    MockspressoConfigContainer configContainer = new MockspressoConfigContainer(
        mMockerConfig,
        mInjectionConfig,
        mDependencyMap,
        mSpecialObjectMakers,
        mRealObjectMapping);
    return new MockspressoImpl(configContainer, dependencyProviderFactory, realObjectMaker);
  }
}
