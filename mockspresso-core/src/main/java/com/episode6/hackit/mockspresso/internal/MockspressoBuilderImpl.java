package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.Dependency;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.CollectionUtil;
import com.episode6.hackit.mockspresso.util.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import org.jetbrains.annotations.Nullable;
import javax.inject.Provider;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link Mockspresso.Builder}
 */
class MockspressoBuilderImpl implements Mockspresso.Builder {

  static final Provider<MockspressoBuilderImpl> PROVIDER = new Provider<MockspressoBuilderImpl>() {
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

  private final RuleConfig mRuleConfig;

  private MockspressoBuilderImpl() {
    mTestResources = new LinkedHashSet<>();
    mDependencyMap = new DependencyMap();
    mSpecialObjectMakers = new SpecialObjectMakerContainer();
    mRealObjectMapping = new RealObjectMapping();

    mMockerConfig = null;
    mInjectionConfig = null;

    mRuleConfig = new RuleConfig();
  }

  // make a "deep" copy of a MockspressoBuilderImpl
  private MockspressoBuilderImpl(MockspressoBuilderImpl copyFrom) {
    mTestResources = new LinkedHashSet<>(copyFrom.mTestResources);
    mDependencyMap = copyFrom.mDependencyMap.deepCopy();
    mSpecialObjectMakers = copyFrom.mSpecialObjectMakers.deepCopy();
    mRealObjectMapping = copyFrom.mRealObjectMapping.deepCopy();

    mMockerConfig = copyFrom.mMockerConfig;
    mInjectionConfig = copyFrom.mInjectionConfig;

    mRuleConfig = copyFrom.mRuleConfig.deepCopy();
  }

  MockspressoBuilderImpl deepCopy() {
    return new MockspressoBuilderImpl(this);
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

  @NotNull
  @Override
  public Mockspresso.Builder plugin(@NotNull MockspressoPlugin plugin) {
    return plugin.apply(this);
  }

  @NotNull
  @Override
  public Mockspresso.Builder outerRule(@NotNull TestRule testRule) {
    mRuleConfig.addOuterRule(testRule);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder outerRule(@NotNull MethodRule methodRule) {
    mRuleConfig.addOuterRule(methodRule);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder innerRule(@NotNull TestRule testRule) {
    mRuleConfig.addInnerRule(testRule);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder innerRule(@NotNull MethodRule methodRule) {
    mRuleConfig.addInnerRule(methodRule);
    return this;
  }

  @NotNull
  public Mockspresso.Builder testResources(@NotNull Object objectWithResources) {
    mTestResources.add(new TestResource(objectWithResources, true));
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder testResourcesWithoutLifecycle(@NotNull Object objectWithResources) {
    mTestResources.add(new TestResource(objectWithResources, false));
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder mocker(@NotNull MockerConfig mockerConfig) {
    mMockerConfig = mockerConfig;
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder injector(@NotNull InjectionConfig injectionConfig) {
    mInjectionConfig = injectionConfig;
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder specialObjectMaker(@NotNull SpecialObjectMaker specialObjectMaker) {
    mSpecialObjectMakers.add(specialObjectMaker);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso.Builder specialObjectMakers(@NotNull List<SpecialObjectMaker> specialObjectMakers) {
    mSpecialObjectMakers.addAll(specialObjectMakers);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependency(@NotNull Class<T> clazz, V value) {
    return dependency(DependencyKey.of(clazz), value);
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependency(@NotNull TypeToken<T> typeToken, V value) {
    return dependency(DependencyKey.of(typeToken), value);
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependency(@NotNull DependencyKey<T> key, V value) {
    mDependencyMap.put(key, value, null);
    return this;
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependencyProvider(@NotNull Class<T> clazz, @NotNull ObjectProvider<V> value) {
    return dependencyProvider(DependencyKey.of(clazz), value);
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependencyProvider(@NotNull TypeToken<T> typeToken, @NotNull ObjectProvider<V> value) {
    return dependencyProvider(DependencyKey.of(typeToken), value);
  }

  @NotNull
  @Override
  public <T, V extends T> Mockspresso.Builder dependencyProvider(@NotNull DependencyKey<T> key, @NotNull ObjectProvider<V> value) {
    mDependencyMap.putProvider(key, value, null);
    return this;
  }

  @NotNull
  @Override
  public <T> Mockspresso.Builder realObject(@NotNull Class<T> objectClass) {
    return realObject(TypeToken.of(objectClass));
  }

  @NotNull
  @Override
  public <T> Mockspresso.Builder realObject(@NotNull TypeToken<T> objectToken) {
    return realObject(DependencyKey.of(objectToken));
  }

  @NotNull
  @Override
  public <T> Mockspresso.Builder realObject(@NotNull DependencyKey<T> keyAndImplementation) {
    return realObject(keyAndImplementation, keyAndImplementation.typeToken);
  }

  @NotNull
  @Override
  public <T> Mockspresso.Builder realObject(@NotNull DependencyKey<T> key, @NotNull Class<? extends T> implementationClass) {
    return realObject(key, TypeToken.of(implementationClass));
  }

  @NotNull
  @Override
  public <T> Mockspresso.Builder realObject(@NotNull DependencyKey<T> key, @NotNull TypeToken<? extends T> implementationToken) {
    mRealObjectMapping.put(key, implementationToken, false);
    return this;
  }

  @NotNull
  @Override
  public Mockspresso build() {
    if (!mRuleConfig.isEmpty()) {
      throw new VerifyError("Tried to add JUnit Rules to a non-rule mockspresso instance.");
    }
    MockspressoInternal instance = deepCopy().buildInternal();
    instance.getConfig().setup(instance);
    return instance;
  }

  @NotNull
  @Override
  public Mockspresso.Rule buildRule() {
    return new MockspressoRuleImpl(
        deepCopy(),
        PROVIDER,
        mRuleConfig.deepCopy());
  }

  MockspressoInternal buildInternal() {
    final MockerConfig mockerConfig =
        Preconditions.assertNotNull(mMockerConfig, "MockerConfig missing from mockspresso builder");
    final InjectionConfig injectionConfig =
        Preconditions.assertNotNull(mInjectionConfig, "InjectionConfig missing from mockspresso builder");
    final SpecialObjectMakerContainer specialObjectMakers = mSpecialObjectMakers;
    final Set<TestResource> testResources = mTestResources;
    final DependencyMap dependencyMap = mDependencyMap;
    final RealObjectMapping realObjectMapping = mRealObjectMapping;

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
        CollectionUtil.concatList(mockerConfig.provideMockAnnotations(), RealObject.class, Dependency.class),
        dependencyMap,
        new DependencyFieldKeyAdjuster());

    RealObjectFieldTracker realObjectFieldTracker = new RealObjectFieldTracker(
        realObjectMapping,
        realObjectMaker,
        dependencyProviderFactory);

    List<ResourcesLifecycleComponent> lifecycleComponents = new LinkedList<>();
    lifecycleComponents.add(
        new ResourcesLifecycleMockManager(
            testResources,
            mockerConfig.provideFieldPreparer()));
    lifecycleComponents.add(
        new ResourceLifecycleFieldManager(
            testResources,
            dependencyMap,
            realObjectMapping, fieldImporter,
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
