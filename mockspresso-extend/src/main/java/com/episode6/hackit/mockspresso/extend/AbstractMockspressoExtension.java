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
 * In the subclass of {@link AbstractMockspressoExtension}, you should only need to override the constructor and
 * the {@link #buildUpon()} method.
 *
 * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
 */
public abstract class AbstractMockspressoExtension<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension<BLDR> {

  private final Mockspresso mDelegate;

  protected AbstractMockspressoExtension(Mockspresso delegate) {
    mDelegate = delegate;
  }

  protected final Mockspresso getDelegate() {
    return mDelegate;
  }

  @Override
  public <T> T create(Class<T> clazz) {
    return getDelegate().create(clazz);
  }

  @Override
  public <T> T create(TypeToken<T> typeToken) {
    return getDelegate().create(typeToken);
  }

  @Override
  public void inject(Object instance) {
    getDelegate().inject(instance);
  }

  /**
   * Extend this abstract class for a custom implementation of the {@link Mockspresso.Rule} interface.
   * In the subclass of {@link AbstractMockspressoExtension.Rule}, you should only need to override the constructor and
   * the {@link #buildUpon()} method.
   *
   * @param <BLDR> Should point to your custom extension of {@link MockspressoExtension.Builder}
   */
  public abstract static class Rule<BLDR extends MockspressoExtension.Builder> implements MockspressoExtension.Rule<BLDR> {

    private final Mockspresso.Rule mDelegate;

    protected Rule(Mockspresso.Rule delegate) {
      mDelegate = delegate;
    }

    protected final Mockspresso.Rule getDelegate() {
      return mDelegate;
    }

    @Override
    public <T> T create(Class<T> clazz) {
      return getDelegate().create(clazz);
    }

    @Override
    public <T> T create(TypeToken<T> typeToken) {
      return getDelegate().create(typeToken);
    }

    @Override
    public void inject(Object instance) {
      getDelegate().inject(instance);
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
      return getDelegate().apply(base, method, target);
    }
  }

  /**
   * Extend this abstract class for a custom implementation of the {@link Mockspresso.Builder} interface.
   * In this class you will need to override the constructor, the {@link #build()} and the {@link #buildRule()}
   * methods. As well as any custom methods you added to your extension of {@link MockspressoExtension.Builder}
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

    protected Builder(Mockspresso.Builder delegate) {
      mDelegate = delegate;
    }

    protected final Mockspresso.Builder getDelegate() {
      return mDelegate;
    }

    @Override
    public BLDR plugin(MockspressoPlugin plugin) {
      getDelegate().plugin(plugin);
      return (BLDR) this;
    }

    @Override
    public BLDR outerRule(TestRule testRule) {
      getDelegate().outerRule(testRule);
      return (BLDR) this;
    }

    @Override
    public BLDR outerRule(MethodRule methodRule) {
      getDelegate().outerRule(methodRule);
      return (BLDR) this;
    }

    @Override
    public BLDR innerRule(TestRule testRule) {
      getDelegate().innerRule(testRule);
      return (BLDR) this;
    }

    @Override
    public BLDR innerRule(MethodRule methodRule) {
      getDelegate().innerRule(methodRule);
      return (BLDR) this;
    }

    @Override
    public BLDR testResources(Object objectWithResources) {
      getDelegate().testResources(objectWithResources);
      return (BLDR) this;
    }

    @Override
    public BLDR testResourcesWithoutLifecycle(Object objectWithResources) {
      getDelegate().testResourcesWithoutLifecycle(objectWithResources);
      return (BLDR) this;
    }

    @Override
    public BLDR mocker(MockerConfig mockerConfig) {
      getDelegate().mocker(mockerConfig);
      return (BLDR) this;
    }

    @Override
    public BLDR injector(InjectionConfig injectionConfig) {
      getDelegate().injector(injectionConfig);
      return (BLDR) this;
    }

    @Override
    public BLDR specialObjectMaker(SpecialObjectMaker specialObjectMaker) {
      getDelegate().specialObjectMaker(specialObjectMaker);
      return (BLDR) this;
    }

    @Override
    public BLDR specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers) {
      getDelegate().specialObjectMakers(specialObjectMakers);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(Class<T> clazz, V value) {
      getDelegate().dependency(clazz, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(TypeToken<T> typeToken, V value) {
      getDelegate().dependency(typeToken, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependency(DependencyKey<T> key, V value) {
      getDelegate().dependency(key, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(Class<T> clazz, ObjectProvider<V> value) {
      getDelegate().dependencyProvider(clazz, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(TypeToken<T> typeToken, ObjectProvider<V> value) {
      getDelegate().dependencyProvider(typeToken, value);
      return (BLDR) this;
    }

    @Override
    public <T, V extends T> BLDR dependencyProvider(DependencyKey<T> key, ObjectProvider<V> value) {
      getDelegate().dependencyProvider(key, value);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(Class<T> objectClass) {
      getDelegate().realObject(objectClass);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(TypeToken<T> objectToken) {
      getDelegate().realObject(objectToken);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> keyAndImplementation) {
      getDelegate().realObject(keyAndImplementation);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> key, Class<? extends T> implementationClass) {
      getDelegate().realObject(key, implementationClass);
      return (BLDR) this;
    }

    @Override
    public <T> BLDR realObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken) {
      getDelegate().realObject(key, implementationToken);
      return (BLDR) this;
    }
  }
}
