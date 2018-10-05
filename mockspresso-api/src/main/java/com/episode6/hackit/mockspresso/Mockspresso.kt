package com.episode6.hackit.mockspresso

import com.episode6.hackit.mockspresso.api.*
import com.episode6.hackit.mockspresso.reflect.DependencyKey
import com.episode6.hackit.mockspresso.reflect.TypeToken
import org.junit.rules.MethodRule
import org.junit.rules.TestRule

/**
 * Main mockspresso interface
 */
interface Mockspresso {

  /**
   * Create a real object injected with mockspresso dependencies.
   * @param clazz The class you want to create an instance of.
   * @param <T> Class type
   * @return An instance of T
  </T> */
  fun <T> create(clazz: Class<T>): T

  /**
   * Create a real object injected with mockspresso dependencies.
   * @param typeToken The class you want to create an instance of.
   * @param <T> Class type
   * @return An instance of T
  </T> */
  fun <T> create(typeToken: TypeToken<T>): T

  /**
   * Inject an existing object with mockspresso dependencies.
   * Field and method injection will be performed (assuming the
   * injector of this mockspresso instance supports it)
   * @param instance The object to be injected.
   */
  fun inject(instance: Any)

  /**
   * Get a dependency (creating a new mock, if needed) from mockspresso.
   * @param key The dependency key to lookup or mock.
   * @param <T> Dependency type
   * @return The dependency or a new mock if it is not present.
  </T> */
  fun <T> getDependency(key: DependencyKey<T>): T

  /**
   * Build upon this mockspresso instance's configuration and dependencies.
   * @return a new [Builder] that is based upon this mockspresso instance.
   */
  fun buildUpon(): Builder

  /**
   * An implementation of Mockspresso that also implements JUnit's [MethodRule].
   */
  interface Rule : Mockspresso, MethodRule

  /**
   * Class used to build Mockspresso and Mockspresso.Rule instances.
   */
  interface Builder {

    /**
     * Apply a [MockspressoPlugin] to this builder
     * @param plugin The plugin to apply
     * @return this
     */
    fun plugin(plugin: MockspressoPlugin): Builder

    /**
     * Treat the resulting [Mockspresso.Rule] as a RuleChain, and wrap it using the
     * supplied testRule as an outerRule.
     *
     * Rules are processed in the order they are added to the builder. So the first outerRule
     * added will be the outermost-rule.
     *
     * @param testRule The testRule that should be chained outside the [Mockspresso.Rule]
     * @return this
     */
    fun outerRule(testRule: TestRule): Builder

    /**
     * Treat the resulting [Mockspresso.Rule] as a RuleChain, and wrap it using the
     * supplied testRule as an outerRule.
     *
     * Rules are processed in the order they are added to the builder. So the first outerRule
     * added will be the outermost-rule.
     *
     * @param methodRule The methodRule that should be chained outside the [Mockspresso.Rule]
     * @return this
     */
    fun outerRule(methodRule: MethodRule): Builder

    /**
     * Treat the resulting [Mockspresso.Rule] as a RuleChain, and wrap the supplied testRule
     * as an innerRule to mockspresso.
     *
     * Rules are processed in the order they are added to the builder. So the first innerRule added
     * will be the outer-most innerRule.
     *
     * @param testRule The testRule that should be chained inside the [Mockspresso.Rule]
     * @return this
     */
    fun innerRule(testRule: TestRule): Builder

    /**
     * Treat the resulting [Mockspresso.Rule] as a RuleChain, and wrap the supplied testRule
     * as an innerRule to mockspresso.
     *
     * Rules are processed in the order they are added to the builder. So the first innerRule added
     * will be the outer-most innerRule.
     *
     * @param methodRule The methodRule that should be chained inside the [Mockspresso.Rule]
     * @return this
     */
    fun innerRule(methodRule: MethodRule): Builder

    /**
     * Scans the included objectWithResources for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map. Will also call any methods annotated with [org.junit.Before] during
     * the initialization process, and any methods annotated with [org.junit.After] during the teardown process.
     *
     * Don't pass the actual test class to this method, as it will result in multiple calls to your @Before
     * and @After methods. Instead use [.testResourcesWithoutLifecycle]
     *
     * @param objectWithResources The object to scan, set fields on and initialize
     * @return this
     */
    fun testResources(objectWithResources: Any): Builder

    /**
     * Scans the included objectWithResources for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map. Mockspresso will not call any methods on objects added via this method.
     * @param objectWithResources The object to scan and set fields on
     * @return this
     */
    fun testResourcesWithoutLifecycle(objectWithResources: Any): Builder

    /**
     * Apply a [MockerConfig] to this builder, which tells mockspresso how to create a mock
     * and how they are annotated.
     * @param mockerConfig The MockerConfig to apply
     * @return this
     */
    fun mocker(mockerConfig: MockerConfig): Builder

    /**
     * Apply a [InjectionConfig] to this builder, which tells mockspresso how to create real objects.
     * @param injectionConfig The InjectionConfig to apply
     * @return this
     */
    fun injector(injectionConfig: InjectionConfig): Builder

    /**
     * Apply a [SpecialObjectMaker] to this builder, which tells mockspresso how it should create
     * object types that should not be mocked by default.
     * @param specialObjectMaker The SpecialObjectMaker to apply
     * @return this
     */
    fun specialObjectMaker(specialObjectMaker: SpecialObjectMaker): Builder

    /**
     * Apply a list of [SpecialObjectMaker]s to this builder, which tells mockspresso how it should create
     * object types that should not be mocked by default.
     * @param specialObjectMakers The SpecialObjectMakers to apply
     * @return this
     */
    fun specialObjectMakers(specialObjectMakers: List<SpecialObjectMaker>): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
    </V></T> */
    fun <T, V : T> dependency(clazz: Class<T>, value: V): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value The instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
    </V></T> */
    fun <T, V : T> dependency(typeToken: TypeToken<T>, value: V): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value The instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
    </V></T> */
    fun <T, V : T> dependency(key: DependencyKey<T>, value: V): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param clazz The class of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> clazz type
     * @param <V> value type
     * @return this
    </V></T> */
    fun <T, V : T> dependencyProvider(clazz: Class<T>, value: ObjectProvider<V>): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param typeToken The typeToken of the dependency we're applying
     * @param value A provider for an instance of the dependency we're applying
     * @param <T> typeToken type
     * @param <V> instanceType
     * @return this
    </V></T> */
    fun <T, V : T> dependencyProvider(typeToken: TypeToken<T>, value: ObjectProvider<V>): Builder

    /**
     * Apply a specific instance of an object as a mockspresso dependency.
     * @param key The DependencyKey of the dependency we're applying.
     * @param value A provider for an instance of the dependency we're applying.
     * @param <T> key type
     * @param <V> instance type
     * @return this
    </V></T> */
    fun <T, V : T> dependencyProvider(key: DependencyKey<T>, value: ObjectProvider<V>): Builder

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectClass), objectClass);
     * @param objectClass The class of the dependency key we should create real objects for
     * @param <T> objectClass type
     * @return this
    </T> */
    fun <T> realObject(objectClass: Class<T>): Builder

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(DependencyKey.of(objectToken), objectToken);
     * @param objectToken The TypeToken of the dependency key we should create real objects for
     * @param <T> objectToken type
     * @return this
    </T> */
    fun <T> realObject(objectToken: TypeToken<T>): Builder

    /**
     * Instruct mockspresso to create a real object for the provided dependency key.
     * Convenience method, equivalent to calling realObject(keyAndImplementation, keyAndImplementation.typeToken);
     * @param keyAndImplementation The [DependencyKey] representing the dependency we should create real objects to fulfil.
     * @param <T> keyAndImplementation type
     * @return this
    </T> */
    fun <T> realObject(keyAndImplementation: DependencyKey<T>): Builder

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The [DependencyKey] representing the dependency we should create real objects to fulfil.
     * @param implementationClass The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
    </T> */
    fun <T> realObject(key: DependencyKey<T>, implementationClass: Class<out T>): Builder

    /**
     * Instruct mockspresso to create a real object (of type implementationClass) for the provided dependency key.
     * @param key The [DependencyKey] representing the dependency we should create real objects to fulfil.
     * @param implementationToken The type of object we mockspresso should create to fulfil dependencies matching key.
     * @param <T> key type
     * @return this
    </T> */
    fun <T> realObject(key: DependencyKey<T>, implementationToken: TypeToken<out T>): Builder

    /**
     * @return an instance of [Mockspresso] that can used to create real objects (and be further built upon)
     */
    fun build(): Mockspresso

    /**
     * @return an instance of [Mockspresso.Rule] that is both an instance of [Mockspresso] and a [org.junit.Rule]
     */
    fun buildRule(): Rule
  }
}
