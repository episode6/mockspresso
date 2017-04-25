package com.episode6.hackit.mockspresso;

import com.episode6.hackit.mockspresso.api.*;
import com.episode6.hackit.mockspresso.reflect.DependencyKey;
import com.episode6.hackit.mockspresso.reflect.TypeToken;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;

import java.lang.annotation.Annotation;
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
   * Build upon this mockspresso instance's configuration and dependencies.
   * @return a new {@link Builder} that is based upon this mockspresso instance.
   */
  Builder buildUpon();

  /**
   * An implementation of Mockspresso that also implements JUnit's {@link MethodRule}.
   */
  interface Rule extends Mockspresso, MethodRule {

    /**
     * Chain a {@link TestRule} inside this Mockspresso.Rule
     * @param testRule The inner test rule to chain
     * @return this Rule with the new RuleChain applied
     */
    Rule chainAround(TestRule testRule);

    /**
     * Chain a {@link MethodRule} inside this Mockspresso.Rule
     * @param methodRule The inner test rule to chain
     * @return this Rule with the new RuleChain applied
     */
    Rule chainAround(MethodRule methodRule);
  }

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
     * Scans the included objectWithFields for fields annotated with @Mock and @RealObject, then prepares them
     * and adds them to our dependency map.
     * @param objectWithFields The object to scan and set fields on (usually a Test class)
     * @return this
     */
    Builder fieldsFrom(Object objectWithFields);

    /**
     * Add an initializer to this mockspresso instance. The initializer will be called just as
     * the instance is completely built, if building a Mockspresso.Rule, then it will execute as
     * part of the rule's statement. Initializers will be executed in order, after all field
     * injection as completed.
     * @param initializer The initializer to add to this mockspresso instance.
     * @return this
     */
    Builder initializer(MockspressoInitializer initializer);

    /**
     * Add an initializer that also has fields for field injection.
     * @see #initializer(MockspressoInitializer) and
     * @see #fieldsFrom(Object) for more info
     * @param initializerWithFields The initializer with fields to add
     * @return this
     */
    Builder initializerWithFields(MockspressoInitializer initializerWithFields);

    /**
     * Apply a {@link MockerConfig} to this builder, which tells mockspresso how to create a mock
     * and how they are annotated.
     * @param mockerConfig The MockerConfig to apply
     * @return this
     */
    Builder mockerConfig(MockerConfig mockerConfig);

    /**
     * Apply a {@link InjectionConfig} to this builder, which tells mockspresso how to create real objects.
     * @param injectionConfig The InjectionConfig to apply
     * @return this
     */
    Builder injectionConfig(InjectionConfig injectionConfig);

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
   * Contains static methods to create new {@link Mockspresso.Builder}s
   */
  class Builders {

    /**
     * @return an empty {@link Mockspresso.Builder} with no configuration applied.
     */
    public static Builder empty() {
      return com.episode6.hackit.mockspresso.internal.MockspressoBuilderImpl.PROVIDER.get();
    }

    /**
     * Start building an instance of Mockspresso designed to create POJOs with no DI.
     * @return a basic instance of {@link Mockspresso.Builder} with the
     * {@link com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin} applied
     */
    public static Builder simple() {
      return empty()
          .plugin(com.episode6.hackit.mockspresso.plugin.simple.SimpleInjectMockspressoPlugin.getInstance());
    }

    /**
     * Start building an instance of Mockspresso designed to create javax.inject compatible DI objects.
     * @return an instance of {@link Mockspresso.Builder} with the
     * {@link com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin} applied.
     */
    public static Builder javaxInjection() {
      return empty()
          .plugin(com.episode6.hackit.mockspresso.plugin.javax.JavaxInjectMockspressoPlugin.getInstance());
    }
  }
}
