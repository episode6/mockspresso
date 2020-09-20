package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * DEPRECATED - kotlin usage is easier to maintain and should be preferred. This module will be removed in a future release.
 *
 * Extend these 3 classes to create your own mockspresso extension
 * {@link AbstractMockspressoExtension}
 * {@link AbstractMockspressoExtension.Rule}
 * {@link AbstractMockspressoExtension.Builder}
 *
 * In each subclass, point the generic type references to your interface extensions of {@link MockspressoExtension}
 * and its inner interfaces. Each subclass should also directly implement the appropriate interface extension
 * of {@link MockspressoExtension}
 *
 * In the subclass of {@link AbstractMockspressoExtension}, you should only need to override the constructor, providing
 * a lambda that wraps {@link Mockspresso.Builder} with your custom subclass of {@link AbstractMockspressoExtension.Builder}
 *
 * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
 */
@Deprecated
public abstract class AbstractMockspressoExtension<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension<BLDR> {

  /**
   * DEPRECATED - kotlin usage is easier to maintain and should be preferred. This module will be removed in a future release.
   *
   * A simple interface that tells us how wrap a {@link Mockspresso} instance with
   * your custom implementation (a subclass of one of these abstract classes.
   * @param <IN> Input type - Either {@link Mockspresso}, {@link Mockspresso.Rule} or {@link Mockspresso.Builder}
   * @param <OUT> Output type - one of your custom mockspresso extension's types
   */
  @Deprecated
  protected interface Wrapper<IN, OUT> {
    @NotNull OUT wrap(@NotNull IN delegate);
  }

  private final Mockspresso mDelegate;
  private final Wrapper<Mockspresso.Builder, BLDR> mBuilderWrapper;

  protected AbstractMockspressoExtension(
      @NotNull Mockspresso delegate,
      @NotNull Wrapper<Mockspresso.Builder, BLDR> builderWrapper) {
    mDelegate = delegate;
    mBuilderWrapper = builderWrapper;
  }

  @NotNull
  @Override
  public <T> T create(@NotNull Class<T> clazz) {
    return mDelegate.create(clazz);
  }

  @NotNull
  @Override
  public <T> T create(@NotNull TypeToken<T> typeToken) {
    return mDelegate.create(typeToken);
  }

  @Override
  public void inject(@NotNull Object instance) {
    mDelegate.inject(instance);
  }

  @Override
  public <T> void inject(@NotNull T instance, @NotNull TypeToken<T> typeToken) {
    mDelegate.inject(instance, typeToken);
  }

  @Override
  public <T> T getDependency(@NotNull DependencyKey<T> key) {
    return mDelegate.getDependency(key);
  }

  @NotNull
  @Override
  public BLDR buildUpon() {
    return mBuilderWrapper.wrap(mDelegate.buildUpon());
  }

  @Override
  public void teardown() {
    mDelegate.teardown();
  }

  /**
   * DEPRECATED - kotlin usage is easier to maintain and should be preferred. This module will be removed in a future release.
   *
   * Extend this abstract class for a custom implementation of the {@link Mockspresso.Rule} interface.
   * In the subclass of {@link AbstractMockspressoExtension.Rule}, you should only need to override the constructor, providing
   * a lambda that wraps {@link Mockspresso.Builder} with your custom subclass of {@link AbstractMockspressoExtension.Builder}
   *
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  @Deprecated
  public abstract static class Rule<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension.Rule<BLDR> {

    private final Mockspresso.Rule mDelegate;
    private final Wrapper<Mockspresso.Builder, BLDR> mBuilderWrapper;

    protected Rule(
        @NotNull Rule delegate,
        @NotNull Wrapper<Builder, BLDR> builderWrapper) {
      mDelegate = delegate;
      mBuilderWrapper = builderWrapper;
    }

    @NotNull
    @Override
    public <T> T create(@NotNull Class<T> clazz) {
      return mDelegate.create(clazz);
    }

    @NotNull
    @Override
    public <T> T create(@NotNull TypeToken<T> typeToken) {
      return mDelegate.create(typeToken);
    }

    @Override
    public void inject(@NotNull Object instance) {
      mDelegate.inject(instance);
    }

    @Override
    public <T> void inject(@NotNull T instance, @NotNull TypeToken<T> typeToken) {
      mDelegate.inject(instance, typeToken);
    }

    @Override
    public <T> T getDependency(@NotNull DependencyKey<T> key) {
      return mDelegate.getDependency(key);
    }

    @NotNull
    @Override
    public BLDR buildUpon() {
      return mBuilderWrapper.wrap(mDelegate.buildUpon());
    }

    @Override
    public void teardown() {
      mDelegate.teardown();
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
      return mDelegate.apply(base, method, target);
    }
  }

  /**
   * DEPRECATED - kotlin usage is easier to maintain and should be preferred. This module will be removed in a future release.
   *
   * Extend this abstract class for a custom implementation of the {@link Mockspresso.Builder} interface.
   * In this class you will only need to override the constructor, providing lambdas that wrap
   * {@link Mockspresso} with your custom subclass of {@link AbstractMockspressoExtension} and
   * {@link Mockspresso.Rule} with your custom subclass of {@link AbstractMockspressoExtension.Rule}.
   *
   * Of course you'll also need to override any custom methods you've added to your api.
   *
   * @param <EXT> Should point to your custom extension of {@link MockspressoExtension}
   * @param <RULE> Should point to your custom extension of {@link MockspressoExtension.Rule}
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public abstract static class Builder<
      EXT extends MockspressoExtension,
      RULE extends MockspressoExtension.Rule,
      BLDR extends MockspressoExtension.Builder> implements MockspressoExtension.Builder<EXT, RULE, BLDR> {

    private final Mockspresso.Builder mDelegate;
    private final Wrapper<Mockspresso, EXT> mExtensionWrapper;
    private final Wrapper<Mockspresso.Rule, RULE> mRuleWrapper;

    protected Builder(
        @NotNull Mockspresso.Builder delegate,
        @NotNull Wrapper<Mockspresso, EXT> extensionWrapper,
        @NotNull Wrapper<Mockspresso.Rule, RULE> ruleWrapper) {
      mDelegate = delegate;
      mExtensionWrapper = extensionWrapper;
      mRuleWrapper = ruleWrapper;
    }

    @NotNull
    @Override
    public EXT build() {
      return mExtensionWrapper.wrap(mDelegate.build());
    }

    @NotNull
    @Override
    public RULE buildRule() {
      return mRuleWrapper.wrap(mDelegate.buildRule());
    }

    @NotNull
    @Override
    public BLDR plugin(@NotNull MockspressoPlugin plugin) {
      mDelegate.plugin(plugin);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR outerRule(@NotNull TestRule testRule) {
      mDelegate.outerRule(testRule);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR outerRule(@NotNull MethodRule methodRule) {
      mDelegate.outerRule(methodRule);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR innerRule(@NotNull TestRule testRule) {
      mDelegate.innerRule(testRule);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR innerRule(@NotNull MethodRule methodRule) {
      mDelegate.innerRule(methodRule);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR testResources(@NotNull Object objectWithResources) {
      mDelegate.testResources(objectWithResources);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR testResourcesWithoutLifecycle(@NotNull Object objectWithResources) {
      mDelegate.testResourcesWithoutLifecycle(objectWithResources);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR mocker(@NotNull MockerConfig mockerConfig) {
      mDelegate.mocker(mockerConfig);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR injector(@NotNull InjectionConfig injectionConfig) {
      mDelegate.injector(injectionConfig);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public BLDR specialObjectMaker(@NotNull SpecialObjectMaker specialObjectMaker) {
      mDelegate.specialObjectMaker(specialObjectMaker);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependency(@NotNull Class<T> clazz, V value) {
      mDelegate.dependency(clazz, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependency(@NotNull TypeToken<T> typeToken, V value) {
      mDelegate.dependency(typeToken, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependency(@NotNull DependencyKey<T> key, V value) {
      mDelegate.dependency(key, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependencyProvider(@NotNull Class<T> clazz, @NotNull ObjectProvider<V> value) {
      mDelegate.dependencyProvider(clazz, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependencyProvider(@NotNull TypeToken<T> typeToken, @NotNull ObjectProvider<V> value) {
      mDelegate.dependencyProvider(typeToken, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T, V extends T> BLDR dependencyProvider(@NotNull DependencyKey<T> key, @NotNull ObjectProvider<V> value) {
      mDelegate.dependencyProvider(key, value);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T> BLDR realObject(@NotNull Class<T> objectClass) {
      mDelegate.realObject(objectClass);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T> BLDR realObject(@NotNull TypeToken<T> objectToken) {
      mDelegate.realObject(objectToken);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T> BLDR realObject(@NotNull DependencyKey<T> keyAndImplementation) {
      mDelegate.realObject(keyAndImplementation);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T> BLDR realObject(@NotNull DependencyKey<T> key, @NotNull Class<? extends T> implementationClass) {
      mDelegate.realObject(key, implementationClass);
      return (BLDR) this;
    }

    @NotNull
    @Override
    public <T> BLDR realObject(@NotNull DependencyKey<T> key, @NotNull TypeToken<? extends T> implementationToken) {
      mDelegate.realObject(key, implementationToken);
      return (BLDR) this;
    }
  }
}
