package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  public static final Provider<MockspressoBuilderImpl> PROVIDER = new Provider<MockspressoBuilderImpl>() {
    @Override
    public MockspressoBuilderImpl get() {
      return new MockspressoBuilderImpl();
    }
  };

  private final List<Object> mObjectsWithResources = new LinkedList<>();
  private final DependencyMap mDependencyMap = new DependencyMap();
  private final SpecialObjectMakerContainer mSpecialObjectMakers = new SpecialObjectMakerContainer();
  private final RealObjectMapping mRealObjectMapping = new RealObjectMapping();

  private @Nullable MockerConfig mMockerConfig = null;
  private @Nullable InjectionConfig mInjectionConfig = null;

  private MockspressoBuilderImpl() {}

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

  public Mockspresso.Builder testResources(Object objectWithResources) {
    mObjectsWithResources.add(objectWithResources);
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
  public <T, V extends T> Mockspresso.Builder dependency(Class<T> clazz, V value) {
    return dependency(DependencyKey.of(clazz), value);
  }

  @Override
  public <T, V extends T> Mockspresso.Builder dependency(TypeToken<T> typeToken, V value) {
    return dependency(DependencyKey.of(typeToken), value);
  }

  @Override
  public <T, V extends T> Mockspresso.Builder dependency(DependencyKey<T> key, V value) {
    mDependencyMap.put(key, value, null);
    return this;
  }

  @Override
  public <T> Mockspresso.Builder realObject(Class<T> objectClass) {
    return realObject(TypeToken.of(objectClass));
  }

  @Override
  public <T> Mockspresso.Builder realObject(TypeToken<T> objectToken) {
    return realObject(DependencyKey.of(objectToken));
  }

  @Override
  public <T> Mockspresso.Builder realObject(DependencyKey<T> keyAndImplementation) {
    return realObject(keyAndImplementation, keyAndImplementation.typeToken);
  }

  @Override
  public <T> Mockspresso.Builder realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
    return realObject(key, TypeToken.of(implementationClass));
  }

  @Override
  public <T> Mockspresso.Builder realObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
    mRealObjectMapping.put(key, implementationToken, false);
    return this;
  }

  @Override
  public Mockspresso build() {
    MockspressoInternal instance = buildInternal();
    instance.getConfig().setup(instance);
    return instance;
  }

  @Override
  public Mockspresso.Rule buildRule() {
    return new MockspressoRuleImpl(
        buildInternal(),
        PROVIDER);
  }

  public MockspressoInternal buildInternal() {
    // verify
    Preconditions.assertNotNull(mMockerConfig, "MockerConfig missing from mockspresso builder");
    Preconditions.assertNotNull(mInjectionConfig, "InjectionConfig missing from mockspresso builder");

    DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mDependencyMap);

    RealObjectMapping realObjectMapping = new RealObjectMapping();
    realObjectMapping.setParentMap(mRealObjectMapping);

    // create the objects we use to create objects
    RealObjectMaker realObjectMaker = new RealObjectMaker(
        mInjectionConfig.provideConstructorSelector(),
        mInjectionConfig.provideInjectableFieldAnnotations(),
        mInjectionConfig.provideInjectableMethodAnnotations());

    DependencyProviderFactory dependencyProviderFactory = new DependencyProviderFactory(
        mMockerConfig.provideMockMaker(),
        dependencyMap,
        mSpecialObjectMakers,
        realObjectMapping,
        realObjectMaker);

    ConfigLifecycle configLifecycle = new ConfigLifecycle(
        dependencyProviderFactory,
        mObjectsWithResources);

    MockspressoConfigContainer configContainer = new MockspressoConfigContainer(
        mMockerConfig,
        mInjectionConfig,
        dependencyMap,
        mSpecialObjectMakers,
        realObjectMapping,
        configLifecycle);

    return new MockspressoImpl(
        configContainer,
        dependencyProviderFactory,
        realObjectMaker,
        PROVIDER);
  }
}
