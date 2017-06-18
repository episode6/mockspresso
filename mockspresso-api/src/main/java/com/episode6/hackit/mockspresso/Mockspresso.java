package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.api.InjectionConfig;
import com.episode6.hackit.mockspresso.api.MockerConfig;
import com.episode6.hackit.mockspresso.api.MockspressoPlugin;
import com.episode6.hackit.mockspresso.api.SpecialObjectMaker;
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
   * @param instance The object to be injected.
   */
  void inject(Object instance);

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
     * Apply one of the built-in {@link MockerConfig}s to this builder.
     * @return A {@link MockerPicker} that will apply a mockerConfig to this builder.
     */
    MockerPicker mocker();

    /**
     * Apply a {@link InjectionConfig} to this builder, which tells mockspresso how to create real objects.
     * @param injectionConfig The InjectionConfig to apply
     * @return this
     */
    Builder injector(InjectionConfig injectionConfig);

    /**
     * Apply one of the built-in {@link InjectionConfig}s to this builder.
     * @return A {@link InjectorPicker} that will apply an injectionConfig to this builder.
     */
    InjectorPicker injector();

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

  /**
   * A selector for one of the built-in mocker configs
   */
  interface MockerPicker {

    /**
     * Applies the {@link MockerConfig} for Mockito.
     * Requires your project have a dependency on 'org.mockito:mockito-core' v2.x
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder mockito();

    /**
     * Applies the {@link MockerConfig} for EasyMock.
     * Requires your project have a dependency on 'org.easymock:easymock' v3.4
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder easyMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito.
     * Requires your project have a dependency on org.mockito:mockito-core v2.x,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder mockitoWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + Mockito AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #mockitoWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder mockitoWithPowerMockRule();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock.
     * Requires your project have a dependency on org.easymock:easymock v3.4,
     * org.powermock:powermock-api-mockito2, and org.powermock:powermock-module-junit4 v1.7.0+.
     * Also requires your test be runWith the PowerMockRunner
     *
     * WARNING, the @org.easymock.Mock annotation may not work correctly when using Mockspresso +
     * easymock + PowerMockRunner, as easymock overwrites Mockspresso's annotated mocks at the last minute.
     * To work around this problem, use powermock's @Mock, @MockNice and @MockStrict annotations instead.
     *
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder easyMockWithPowerMock();

    /**
     * Applies the {@link MockerConfig} for Powermock + EasyMock AND applies a PowerMockRule as
     * an outerRule to Mockspresso, so theres no need to use the PowerMockRunner
     * Requires your project have the same dependencies as {@link #easyMockWithPowerMock()}
     * PLUS org.powermock:powermock-module-junit4-rule and org.powermock:powermock-classloading-xstream
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder easyMockWithPowerMockRule();
  }

  /**
   * A selector for one of the built in injection configs
   */
  interface InjectorPicker {

    /**
     * Applies the {@link InjectionConfig} for simple creation of objects via
     * their shortest constructor.
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder simple();

    /**
     * Applies the {@link InjectionConfig} for javax.inject based object creation
     * (looks for constructors, fields and methods annotated with @Inject).
     * @return The {@link Builder} for your mockspresso instance
     */
    Builder javax();
  }

}
