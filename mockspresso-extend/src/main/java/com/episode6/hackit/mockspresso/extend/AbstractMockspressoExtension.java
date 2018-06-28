package com.episode6.hackit.mockspresso.extend;

import com.episode6.hackit.mockspresso.Mockspresso;
import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.List;

/**
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
public abstract class AbstractMockspressoExtension<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension<BLDR> {

  /**
   * A simple interface that tells us how wrap a {@link Mockspresso} instance with
   * your custom implementation (a subclass of one of these abstract classes.
   * @param <IN> Input type - Either {@link Mockspresso}, {@link Mockspresso.Rule} or {@link Mockspresso.Builder}
   * @param <OUT> Output type - one of your custom mockspresso extension's types
   */
  protected interface Wrapper<IN, OUT> {
    OUT wrap(IN delegate);
  }

  private final Mockspresso mDelegate;
  private final Wrapper<Mockspresso.Builder, BLDR> mBuilderWrapper;

  protected AbstractMockspressoExtension(
      Mockspresso delegate,
      Wrapper<Mockspresso.Builder, BLDR> builderWrapper) {
    mDelegate = delegate;
    mBuilderWrapper = builderWrapper;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return mDelegate.create(clazz);
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return mDelegate.create(typeToken);
  }

  @Override
  public void inject(Object instance) {
    mDelegate.inject(instance);
  }

  @Override
  public <T> T getDependency(DependencyKey<T> key) {
    return mDelegate.getDependency(key);
  }

  @Override
  public BLDR buildUpon() {
    return mBuilderWrapper.wrap(mDelegate.buildUpon());
  }

  /**
   * Extend this abstract class for a custom implementation of the {@link Mockspresso.Rule} interface.
   * In the subclass of {@link AbstractMockspressoExtension.Rule}, you should only need to override the constructor, providing
   * a lambda that wraps {@link Mockspresso.Builder} with your custom subclass of {@link AbstractMockspressoExtension.Builder}
   *
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  public abstract static class Rule<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension.Rule<BLDR> {

    private final Mockspresso.Rule mDelegate;
    private final Wrapper<Mockspresso.Builder, BLDR> mBuilderWrapper;

    protected Rule(
        Rule delegate,
        Wrapper<Builder, BLDR> builderWrapper) {
      mDelegate = delegate;
      mBuilderWrapper = builderWrapper;
    }

    @Override
    public <T> T create(Class<T> clazz) {
      return mDelegate.create(clazz);
    }

    @Override
    public <T> T create(TypeToken<T> typeToken) {
      return mDelegate.create(typeToken);
    }

    @Override
    public void inject(Object instance) {
      mDelegate.inject(instance);
    }

    @Override
    public <T> T getDependency(DependencyKey<T> key) {
      return mDelegate.getDependency(key);
    }

    @Override
    public BLDR buildUpon() {
      return mBuilderWrapper.wrap(mDelegate.buildUpon());
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
      return mDelegate.apply(base, method, target);
    }
  }

  /**
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
  public abstract static class Builder<
      EXT extends MockspressoExtension,
      RULE extends MockspressoExtension.Rule,
      BLDR extends MockspressoExtension.Builder> implements MockspressoExtension.Builder<EXT, RULE, BLDR> {

    private final Mockspresso.Builder mDelegate;
    private final Wrapper<Mockspresso, EXT> mExtensionWrapper;
    private final Wrapper<Mockspresso.Rule, RULE> mRuleWrapper;

    protected Builder(
        Mockspresso.Builder delegate,
        Wrapper<Mockspresso, EXT> extensionWrapper,
        Wrapper<Mockspresso.Rule, RULE> ruleWrapper) {
      mDelegate = delegate;
      mExtensionWrapper = extensionWrapper;
      mRuleWrapper = ruleWrapper;
    }

    @Override
    public EXT build() {
      return mExtensionWrapper.wrap(mDelegate.build());
    }

    @Override
    public RULE buildRule() {
      return mRuleWrapper.wrap(mDelegate.buildRule());
    }

    @Override
    public BLDR plugin(MockspressoPlugin plugin) {
      mDelegate.plugin(plugin);
      return (BLDR) this;
    }

    @Override
    public BLDR outerRule(TestRule testRule) {
      mDelegate.outerRule(testRule);
      return (BLDR) this;
    }

    @Override
    public BLDR outerRule(MethodRule methodRule) {
      mDelegate.outerRule(methodRule);
      return (BLDR) this;
    }

    @Override
    public BLDR innerRule(TestRule testRule) {
      mDelegate.innerRule(testRule);
      return (BLDR) this;
    }

    @Override
    public BLDR innerRule(MethodRule methodRule) {
      mDelegate.innerRule(methodRule);
      return (BLDR) this;
    }

    @Override
    public BLDR testResources(Object objectWithResources) {
      mDelegate.testResources(objectWithResources);
      return (BLDR) this;
    }

    @Override
    public BLDR testResourcesWithoutLifecycle(Object objectWithResources) {
      mDelegate.testResourcesWithoutLifecycle(objectWithResources);
      return (BLDR) this;
    }

    @Override
    public BLDR mocker(MockerConfig mockerConfig) {
      mDelegate.mocker(mockerConfig);
      return (BLDR) this;
    }

    @Override
    public BLDR injector(InjectionConfig injectionConfig) {
      mDelegate.injector(injectionConfig);
      return (BLDR) this;
    }

    @Override
    public BLDR specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
      mDelegate.specialObjectMaker(specialObjectMaker);
      return (BLDR) this;
    }

    @Override
    public BLDR specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
      mDelegate.specialObjectMakers(specialObjectMakers);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(Class<T> clazz, V value) {
      mDelegate.dependency(clazz, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(TypeToken<T> typeToken, V value) {
      mDelegate.dependency(typeToken, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(DependencyKey<T> key, V value) {
      mDelegate.dependency(key, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(Class<T> clazz, ObjectProvider<V> value) {
      mDelegate.dependencyProvider(clazz, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(TypeToken<T> typeToken, ObjectProvider<V> value) {
      mDelegate.dependencyProvider(typeToken, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(DependencyKey<T> key, ObjectProvider<V> value) {
      mDelegate.dependencyProvider(key, value);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(Class<T> objectClass) {
      mDelegate.realObject(objectClass);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(TypeToken<T> objectToken) {
      mDelegate.realObject(objectToken);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> keyAndImplementation) {
      mDelegate.realObject(keyAndImplementation);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
      mDelegate.realObject(key, implementationClass);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
      mDelegate.realObject(key, implementationToken);
      return (BLDR) this;
    }
  }
}
