package com.episode6.hackit.mockspresso.internal;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.annotation.Dependency;
import com.episode6.hackit.mockspresso.annotation.RealObject;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import com.episode6.hackit.mockspresso.util.CollectionUtil;
import com.episode6.hackit.mockspresso.util.Preconditions;
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

  @Override
  public Mockspresso.Builder plugin(MockspressoPlugin plugin) {
    return plugin.apply(this);
  }

  @Override
  public Mockspresso.Builder outerRule(TestRule testRule) {
    mRuleConfig.addOuterRule(testRule);
    return this;
  }

  @Override
  public Mockspresso.Builder outerRule(MethodRule methodRule) {
    mRuleConfig.addOuterRule(methodRule);
    return this;
  }

  @Override
  public Mockspresso.Builder innerRule(TestRule testRule) {
    mRuleConfig.addInnerRule(testRule);
    return this;
  }

  @Override
  public Mockspresso.Builder innerRule(MethodRule methodRule) {
    mRuleConfig.addInnerRule(methodRule);
    return this;
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
  public Mockspresso.Builder mocker(MockerConfig mockerConfig) {
    mMockerConfig = mockerConfig;
    return this;
  }

  @Override
  public Mockspresso.Builder injector(InjectionConfig injectionConfig) {
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
  public <T, V extends T> Mockspresso.Builder dependencyProvider(Class<T> clazz, ObjectProvider<V> value) {
    return dependencyProvider(DependencyKey.of(clazz), value);
  }

  @Override
  public <T, V extends T> Mockspresso.Builder dependencyProvider(TypeToken<T> typeToken, ObjectProvider<V> value) {
    return dependencyProvider(DependencyKey.of(typeToken), value);
  }

  @Override
  public <T, V extends T> Mockspresso.Builder dependencyProvider(DependencyKey<T> key, ObjectProvider<V> value) {
    mDependencyMap.putProvider(key, value, null);
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
    if (!mRuleConfig.isEmpty()) {
      throw new VerifyError("Tried to add JUnit Rules to a non-rule mockspresso instance.");
    }
    MockspressoInternal instance = deepCopy().buildInternal();
    instance.getConfig().setup(instance);
    return instance;
  }

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
