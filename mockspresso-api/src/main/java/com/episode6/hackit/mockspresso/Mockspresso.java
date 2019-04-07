package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

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
  @NotNull <T> T create(@NotNull Class<T> clazz);

  /**
   * Create a real object injected with mockspresso dependencies.
   * @param typeToken The class you want to create an instance of.
   * @param <T> Class type
   * @return An instance of T
   */
  @NotNull <T> T create(@NotNull TypeToken<T> typeToken);

  /**
   * Inject an existing object with mockspresso dependencies.
   * Field and method injection will be performed (assuming the
   * injector of this mockspresso instance supports it)
   * @param instance The object to inject mocks/dependencies into.
   */
  void inject(@NotNull Object instance);

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
  <T> void inject(@NotNull T instance, @NotNull TypeToken<T> typeToken);

  /**
   * Get a dependency (creating a new mock, if needed) from mockspresso.
   *
   * We purposefully don't annotate the return-type here NotNull
   * because nulls are theoretically allowed dependencies (they just must
   * be mapped explicitly). So kotlin users must assume nullability via
   * platform-type.
   *
   * @param key The dependency key to lookup or mock.
   * @param <T> Dependency type
   * @return The dependency or a new mock if it is not present.
   */
  <T> T getDependency(@NotNull DependencyKey<T> key);

  /**
   * Build upon this mockspresso instance's configuration and dependencies.
   * @return a new {@link Builder} that is based upon this mockspresso instance.
   */
  @NotNull Builder buildUpon();

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
    @NotNull Builder plugin(@NotNull MockspressoPlugin plugin);

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
    @NotNull Builder outerRule(@NotNull TestRule testRule);

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
    @NotNull Builder outerRule(@NotNull MethodRule methodRule);

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
    @NotNull Builder innerRule(@NotNull TestRule testRule);

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
    @NotNull Builder innerRule(@NotNull MethodRule methodRule);

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
    @NotNull Builder testResources(@NotNull Object objectWithResources);

    /**
     * Scans the included objectWithResources for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map. Mockspresso will not call any methods on objects added via this method.
     * @param objectWithResources The object to scan and set fields on
     * @return this
     */
    @NotNull Builder testResourcesWithoutLifecycle(@NotNull Object objectWithResources);

    /**
     * Apply a {@link MockerConfig} to this builder, which tells mockspresso how to create a mock
     * and how they are annotated.
     * @param mockerConfig The MockerConfig to apply
     * @return this
     */
    @NotNull Builder mocker(@NotNull MockerConfig mockerConfig);

    /**
     * Apply a {@link InjectionConfig} to this builder, which tells mockspresso how to create real objects.
     * @param injectionConfig The InjectionConfig to apply
     * @return this
     */
    @NotNull Builder injector(@NotNull InjectionConfig injectionConfig);

    /**
     * Apply a {@link SpecialObjectMaker} to this builder, which tells mockspresso how it should create
     * object types that should not be mocked by default.
     * @param specialObjectMaker The SpecialObjectMaker to apply
     * @return this
     */
    @NotNull Builder specialObjectMaker(@NotNull SpecialObjectMaker specialObjectMaker);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
     */
    @NotNull <T, V extends T> Builder dependency(@NotNull Class<T> clazz, @Nullable V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
     */
    @NotNull <T, V extends T> Builder dependency(@NotNull TypeToken<T> typeToken, @Nullable V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value The instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
     */
    @NotNull <T, V extends T> Builder dependency(@NotNull DependencyKey<T> key, @Nullable V value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
     */
    @NotNull <T, V extends T> Builder dependencyProvider(@NotNull Class<T> clazz, @NotNull ObjectProvider<V> value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
     */
    @NotNull <T, V extends T> Builder dependencyProvider(@NotNull TypeToken<T> typeToken, @NotNull ObjectProvider<V> value);

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value A provider for an instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
     */
    @NotNull <T, V extends T> Builder dependencyProvider(@NotNull DependencyKey<T> key, @NotNull ObjectProvider<V> value);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectClass), objectClass);
     * @param objectClass The class of the dependency key we should create real objects for
     * @param <T> objectClass type
     * @return this
     */
    @NotNull <T> Builder realObject(@NotNull Class<T> objectClass);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectToken), objectToken);
     * @param objectToken The TypeToken of the dependency key we should create real objects for
     * @param <T> objectToken type
     * @return this
     */
    @NotNull <T> Builder realObject(@NotNull TypeToken<T> objectToken);

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(keyAndImplementation, keyAndImplementation.typeToken);
     * @param keyAndImplementation The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param <T> keyAndImplementation type
     * @return this
     */
    @NotNull <T> Builder realObject(@NotNull DependencyKey<T> keyAndImplementation);

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param implementationClass The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
     */
    @NotNull <T> Builder realObject(@NotNull DependencyKey<T> key, @NotNull Class<? extends T> implementationClass);

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The {@link DependencyKey} representing the dependency we should create real objects to fulfil.
     * @param implementationToken The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
     */
    @NotNull <T> Builder realObject(@NotNull DependencyKey<T> key, @NotNull TypeToken<? extends T> implementationToken);

    /**
     * @return an instance of {@link Mockspresso} that can used to create real objects (and be further built upon)
     */
    @NotNull Mockspresso build();

    /**
     * @return an instance of {@link Mockspresso.Rule} that is both an instance of {@link Mockspresso} and a {@link org.junit.Rule}
     */
    @NotNull Rule buildRule();
  }
}
