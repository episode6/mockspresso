package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.util.List;

/**
 * Main mockspresso interface
 */
public interface Mockspresso {

  /**
   * Create a real object injected with mockspresso dependencies.
   * @param clazz The class you want to create an instance of.
   * @param <T> Class type
   * @return An instance of T
   */
  <T> T create(Class<T> clazz);

  /**
   * Create a real object injected with mockspresso dependencies.
   * @param typeToken The class you want to create an instance of.
   * @param <T> Class type
   * @return An instance of T
   */
  <T> T create(TypeToken<T> typeToken);

  /**
   * Inject an existing object with mockspresso dependencies.
   * Field and method injection will be performed (assuming the
   * injector of this mockspresso instance supports it)
   * @param instance The object to inject mocks/dependencies into.
   */
  void inject(Object instance);

  /**
   * An alternative signature to {@link #inject(Object)}. Use this
   * method and pass an explic type token if injecting a pre-
   * constructed generic object that injects parameters defined as
   * TypeVariables. I.e.
   * {@code class MySampleGeneric<V> { @Inject V myInjectedVariable; } }
   * Without the typeToken represented the object being injected, we're
   * unable to infer the correct type for the generic paremeter, and may
   * wind up providing a mock {@link Object} instead of the correct mapping.
   * @param instance The object to inject mocks/dependencies into.
   * @param typeToken A TypeToken representing the complete type of instance
   * @param <T> The Type of instance param
   */
  <T> void inject(T instance, TypeToken<T> typeToken);

  /**
   * Get a dependency (creating a new mock, if needed) from mockspresso.
   * @param key The dependency key to lookup or mock.
   * @param <T> Dependency type
   * @return The dependency or a new mock if it is not present.
   */
  <T> T getDependency(DependencyKey<T> key);

  /**
   * Build upon this mockspresso instance's configuration and dependencies.
   * @return a new {@link Builder} that is based upon this mockspresso instance.
   */
  Builder buildUpon();

  /**
   * An implementation of Mockspresso that also implements JUnit's {@link MethodRule}.
   */
  interface Rule extends Mockspresso, MethodRule {}

  /**
   * Class used to build Mockspresso and Mockspresso.Rule instances.
   */
  interface Builder {

    /**
     * Apply a {@link MockspressoPlugin} to this builder
     * @param plugin The plugin to apply
     * @return this
     */
    Builder plugin(MockspressoPlugin plugin);

    /**
     * Treat the resulting {@link Mockspresso.Rule} as a RuleChain, and wrap it using the
     * supplied testRule as an outerRule.
     *
     * Rules are processed in the order they are added to the builder. So the first outerRule
     * added will be the outermost-rule.
     *
     * @param testRule The testRule that should be chained outside the {@link Mockspresso.Rule}
     * @return this
     */
    Builder outerRule(TestRule testRule);

    /**
     * Treat the resulting {@link Mockspresso.Rule} as a RuleChain, and wrap it using the
     * supplied testRule as an outerRule.
     *
     * Rules are processed in the order they are added to the builder. So the first outerRule
     * added will be the outermost-rule.
     *
     * @param methodRule The methodRule that should be chained outside the {@link Mockspresso.Rule}
     * @return this
     */
    Builder outerRule(MethodRule methodRule);

    /**
     * Treat the resulting {@link Mockspresso.Rule} as a RuleChain, and wrap the supplied testRule
     * as an innerRule to mockspresso.
     *
     * Rules are processed in the order they are added to the builder. So the first innerRule added
     * will be the outer-most innerRule.
     *
     * @param testRule The testRule that should be chained inside the {@link Mockspresso.Rule}
     * @return this
     */
    Builder innerRule(TestRule testRule);

    /**
     * Treat the resulting {@link Mockspresso.Rule} as a RuleChain, and wrap the supplied testRule
     * as an innerRule to mockspresso.
     *
     * Rules are processed in the order they are added to the builder. So the first innerRule added
     * will be the outer-most innerRule.
     *
     * @param methodRule The methodRule that should be chained inside the {@link Mockspresso.Rule}
     * @return this
     */
    Builder innerRule(MethodRule methodRule);

    /**
     * Scans the included objectWithResources for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map. Will also call any methods annotated with {@link org.junit.Before} during
     * the initialization process, and any methods annotated with {@link org.junit.After} during the teardown process.
     *
     * Don't pass the actual test class to this method, as it will result in multiple calls to your @Before
     * and @After methods. Instead use {@link #testResourcesWithoutLifecycle(Object)}
     *
     * @param objectWithResources The object to scan, set fields on and initialize
     * @return this
     */
    Builder testResources(Object objectWithResources);

    /**
     * Scans the included objectWithResources for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map. Mockspresso will not call any methods on objects added via this method.
     * @param objectWithResources The object to scan and set fields on
     * @return this
     */
    Builder testResourcesWithoutLifecycle(Object objectWithResources);

    /**
     * Apply a {@link MockerConfig} to this builder, which tells mockspresso how to create a mock
     * and how they are annotated.
     * @param mockerConfig The MockerConfig to apply
     * @return this
     */
    Builder mocker(MockerConfig mockerConfig);

    /**
     * Apply a {@link InjectionConfig} to this builder, which tells mockspresso how to create real objects.
     * @param injectionConfig The InjectionConfig to apply
     * @return this
     */
    Builder injector(InjectionConfig injectionConfig);

    /**
     * Apply a {@link SpecialObjectMaker} to this builder, which tells mockspresso how it should create
     * object types that should not be mocked by default.
     * @param specialObjectMaker The SpecialObjectMaker to apply
     * @return this
     */
    Builder specialObjectMaker(SpecialObjectMaker specialObjectMaker);

    /**
     * Apply a list of {@link SpecialObjectMaker}s to this builder, which tells mockspresso how it should create
     * object types that should not be mocked by default.
     * @param specialObjectMakers The SpecialObjectMakers to apply
     * @return this
     */
    Builder specialObjectMakers(List<SpecialObjectMaker> specialObjectMakers);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
     */
    <T, V extends T> Builder dependency(Class<T> clazz, V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
     */
    <T, V extends T> Builder dependency(TypeToken<T> typeToken, V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value The instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
     */
    <T, V extends T> Builder dependency(DependencyKey<T> key, V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
     */
    <T, V extends T> Builder dependencyProvider(Class<T> clazz, ObjectProvider<V> value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
     */
    <T, V extends T> Builder dependencyProvider(TypeToken<T> typeToken, ObjectProvider<V> value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value A provider for an instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
     */
    <T, V extends T> Builder dependencyProvider(DependencyKey<T> key, ObjectProvider<V> value);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectClass), objectClass);
     * @param objectClass The class of the dependency key we should create real objects for
     * @param <T> objectClass type
     * @return this
     */
    <T> Builder realObject(Class<T> objectClass);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectToken), objectToken);
     * @param objectToken The TypeToken of the dependency key we should create real objects for
     * @param <T> objectToken type
     * @return this
     */
    <T> Builder realObject(TypeToken<T> objectToken);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(keyAndImplementation, keyAndImplementation.typeToken);
     * @param keyAndImplementation The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param <T> keyAndImplementation type
     * @return this
     */
    <T> Builder realObject(DependencyKey<T> keyAndImplementation);

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param implementationClass The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
     */
    <T> Builder realObject(DependencyKey<T> key, Class<? extends T> implementationClass);

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param implementationToken The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
     */
    <T> Builder realObject(DependencyKey<T> key, TypeToken<? extends T> implementationToken);

    /**
     * @return an instance of {@link Mockspresso} that can used to create real objects (and be further built upon)
     */
    Mockspresso build();

    /**
     * @return an instance of {@link Mockspresso.Rule} that is both an instance of {@link Mockspresso} and a {@link org.junit.Rule}
     */
    Rule buildRule();
  }
}
