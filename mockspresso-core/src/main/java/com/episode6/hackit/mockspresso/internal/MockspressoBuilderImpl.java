package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.CollectionUtil;
import com.episode6.hackit.mockspresso.util.Preconditions;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of {@link Mockspresso.Builder}
 */
public class MockspressoBuilderImpl implements Mockspresso.Builder {

  public static final Provider<MockspressoBuilderImpl> PROVIDER = new Provider<MockspressoBuilderImpl>() {
    @Override
    public MockspressoBuilderImpl get() {
      return new MockspressoBuilderImpl();
    }
  };

  private final LinkedHashSet<TestResource> mTestResources;
  private final DependencyMap mDependencyMap;
  private final SpecialObjectMakerContainer mSpecialObjectMakers;
  private final RealObjectMapping mRealObjectMapping;

  private @Nullable MockerConfig mMockerConfig;
  private @Nullable InjectionConfig mInjectionConfig;
  private @Nullable TestResource mTestClass;

  private MockspressoBuilderImpl() {
    mTestResources = new LinkedHashSet<>();
    mDependencyMap = new DependencyMap();
    mSpecialObjectMakers = new SpecialObjectMakerContainer();
    mRealObjectMapping = new RealObjectMapping();

    mMockerConfig = null;
    mInjectionConfig = null;
    mTestClass = null;
  }

  // make a "deep" copy of a MockspressoBuilderImpl
  private MockspressoBuilderImpl(MockspressoBuilderImpl copyFrom) {
    mTestResources = new LinkedHashSet<>(copyFrom.mTestResources);
    mDependencyMap = copyFrom.mDependencyMap.deepCopy();
    mSpecialObjectMakers = copyFrom.mSpecialObjectMakers.deepCopy();
    mRealObjectMapping = copyFrom.mRealObjectMapping.deepCopy();

    mMockerConfig = copyFrom.mMockerConfig;
    mInjectionConfig = copyFrom.mInjectionConfig;
    mTestClass = copyFrom.mTestClass;
  }

  void setParent(MockspressoConfigContainer parentConfig) {
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

  // internal method used by MockspressoRuleImpl
  void setTestClass(@Nullable Object testClass) {
    mTestClass = testClass == null ? null : new TestResource(testClass, false);
  }

  @Override
  public Mockspresso.Builder plugin(MockspressoPlugin plugin) {
    return plugin.apply(this);
  }

  public Mockspresso.Builder testResources(Object objectWithResources) {
    mTestResources.add(new TestResource(objectWithResources, true));
    return this;
  }

  @Override
  public Mockspresso.Builder testResourcesWithoutLifecycle(Object objectWithResources) {
    mTestResources.add(new TestResource(objectWithResources, false));
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
        new MockspressoBuilderImpl(this),
        PROVIDER);
  }

  MockspressoInternal buildInternal() {
    final MockerConfig mockerConfig =
        Preconditions.assertNotNull(mMockerConfig, "MockerConfig missing from mockspresso builder");
    final InjectionConfig injectionConfig =
        Preconditions.assertNotNull(mInjectionConfig, "InjectionConfig missing from mockspresso builder");
    final SpecialObjectMakerContainer specialObjectMakers = mSpecialObjectMakers;
    final List<TestResource> testResources = CollectionUtil.concatList(mTestResources, mTestClass);

    // DependencyMap and RealObjectMapping will be added to (and potentially cleared)
    // during the mockspresso lifecycle, so we separate any explicitly defined dependencies
    // and real object mappings from those added by test resources during the last-mile setup
    // process.
    final DependencyMap dependencyMap = new DependencyMap();
    dependencyMap.setParentMap(mDependencyMap);
    final RealObjectMapping realObjectMapping = new RealObjectMapping();
    realObjectMapping.setParentMap(mRealObjectMapping);

    RealObjectMaker realObjectMaker = new RealObjectMaker(
        injectionConfig.provideConstructorSelector(),
        injectionConfig.provideInjectableFieldAnnotations(),
        injectionConfig.provideInjectableMethodAnnotations());

    DependencyProviderFactory dependencyProviderFactory = new DependencyProviderFactory(
        mockerConfig.provideMockMaker(),
        dependencyMap,
        specialObjectMakers,
        realObjectMapping,
        realObjectMaker);

    FieldImporter fieldImporter = new FieldImporter(
        CollectionUtil.concatList(mockerConfig.provideMockAnnotations(), RealObject.class),
        dependencyMap);

    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        realObjectMapping,
        dependencyProviderFactory.getBlankDependencyProvider());

    List<ResourcesLifecycleComponent> lifecycleComponents = new LinkedList<>();
    lifecycleComponents.add(
        new ResourcesLifecycleMockManager(
            testResources,
            mockerConfig.provideFieldPreparer()));
    lifecycleComponents.add(
        new ResourceLifecycleFieldManager(
            testResources,
            dependencyMap,
            fieldImporter,
            realObjectFieldTracker));
    lifecycleComponents.add(
        new ResourcesLifecycleMethodManager(
            testResources));

    ResourcesLifecycleManager resourcesLifecycleManager = new ResourcesLifecycleManager(
        lifecycleComponents);

    MockspressoConfigContainer configContainer = new MockspressoConfigContainer(
        mockerConfig,
        injectionConfig,
        dependencyMap,
        specialObjectMakers,
        realObjectMapping,
        resourcesLifecycleManager);

    return new MockspressoImpl(
        configContainer,
        dependencyProviderFactory,
        realObjectMaker,
        PROVIDER);
  }
}
