package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.*;
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
    return dependency(TypeToken.of(clazz), null, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(TypeToken<T> typeToken, T value) {
    return dependency(typeToken, null, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(Class<T> clazz, Annotation annotation, T value) {
    return dependency(TypeToken.of(clazz), annotation, value);
  }

  @Override
  public <T> Mockspresso.Builder dependency(TypeToken<T> typeToken, Annotation annotation, T value) {
    mDependencyMap.put(new DependencyKey<T>(typeToken, annotation), value);
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
    DependencyProvider dependencyProvider = new DependencyProviderImpl(
        mMockerConfig.provideMockMaker(),
        mDependencyMap,
        mSpecialObjectMakers);

    // prepare mObjectsWithFields
    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        realObjectMaker,
        mDependencyMap,
        dependencyProvider);
    MockerConfig.FieldPreparer mockFieldPreparer = mMockerConfig.provideFieldPreparer();
    List<Class<? extends Annotation>> mockAnnotations = mMockerConfig.provideMockAnnotations();
    for (Object o : mObjectsWithFields) {
      // prepare mock fields
      mockFieldPreparer.prepareFields(o);

      // import mocks and non-null real objects into dependency map
      mDependencyMap.importFrom()
          .annotatedFields(o, mockAnnotations)
          .annotatedFields(o, RealObject.class);

      // track down any @RealObjects that are null
      realObjectFieldTracker.scanNullRealObjectFields(o);
    }

    // build missing real objects, and assign them
    realObjectFieldTracker.createAndAssignTrackedRealObjects();

    MockspressoConfigContainer configContainer = new MockspressoConfigContainer(
        mMockerConfig,
        mInjectionConfig,
        mDependencyMap,
        mSpecialObjectMakers,
        mRealObjectMapping);
    return new MockspressoImpl(configContainer, dependencyProvider, realObjectMaker);
  }
}
