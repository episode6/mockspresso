## Getting Started
To create a new [`Mockspresso`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/index.html) instance we start with an empty [`Mockspresso.Builder()`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/index.html) and because mockspresso is agnostic to our DI and mocking framework of choice, we must teach it how to create mocks and real objects. We usually do this using kotlin extension functions, but also provide plugins for java callers that can be applied using [`Mockspresso.Builder.plugin(MockspressoPlugin)`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/plugin.html). As a pattern, Java support plugins are provided by identically named methods that return [`MockspressoPlugin`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso.api/-mockspresso-plugin/index.html)s and are invisible to kotlin.

**Kotlin Example**
```kotlin
BuildMockspresso.with()
    .injectBySimpleConfig()
    .mockByMockito()
```

**Java Example**
```java
BuildMockspresso.with()
    .plugin(MockspressoBasicPluginsJavaSupport.injectBySimpleConfig())
    .plugin(MockspressoBasicPluginsJavaSupport.mockByMockito())
```

All Mockspresso.Builders require both an Injector and a Mocker in order to build a mockspresso instance. See [INCLUDED_PLUGINS](INCLUDED_PLUGINS.md) for a list of included injectors and mockers.

**Project Entry-Point**

Since most of the tests in a single project are likely to use the same mocker & injector, it's a best-practice to set up a single entry-point for all of a project's mockspresso tests...
```kotlin
object BuildMockspresso {
  @JvmStatic
  fun withDefaults(): Builder = com.episode6.hackit.mockspresso.BuildMockspresso.with()
      .injectBySimpleConfig()
      .mockByMockito()
}
```

### JUnit Rule
The simplest way to set up a mockspresso test is by applying a JUnit Rule via [`Mockspresso.Builder.buildRule()`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/build-rule.html). Applying the rule will automatically trigger annotation processing on your test for mock generation, dependency import and real object creation.

```kotlin
// kotlin with mockito/mockito-kotlin
class CoffeeMakerHeaterTest {

    // Note: the `@get:` syntax is required of all junit rules in kotlin tests
    @get:Rule val mockspresso = BuildMockspresso.withDefaults().buildRule()

    // declare only the mocks we need for our test
    @Dependency val heater: Heater = mock()

    // real object will be created for us
    @RealObject lateinit var coffeeMaker: CoffeeMaker

    @Test fun testHeaterIsUser() {
        val coffee = coffeeMaker.brew()

        verify(heater).heat(any())
    }
}
```
Note that there is also a `build()` method available, but most unit tests will find `buildRule()` more convenient.

**TODO** LINK TO DOC ON ON-THE-FLY USAGE

### Annotations

When using the mockspresso junit rule, mockspresso will perform some reflection-based annotation processing on your test class.

[**@Dependency**](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso.annotation/-dependency/index.html): Marks a final field or val that should be part of the mockspresso dependency graph.
```kotlin
// supply this TestHeater wherever a Heater is injected
@Dependency val heater: Heater = TestHeater()

// supply a mock using mockito-kotlin syntax
@Dependency val waterFilter: WaterFilter = mock()

// (rx example) bind this TestScheduler as a Scheduler in the graph but
// retain access to all TestScheduler methods in these tests.
@Dependency(bindAs = Scheduler::class) val testScheduler = TestScheduler()

// Apply a qualifier annotation to the field if the object-under-test uses one to inject the dependency.
@Dependency @field:Named("brew_temp") val brewTemperature: Int = 100
```

**@Mock**: EasyMock & Mockito's `@Mock` annotations are automatically recognized as dependencies when the appropriate mocker is used. The mockspresso rule will also take care of initializing the mocks, so there's no need to apply the EasyMock or Mockito rules or init annotations.
```kotlin
// Automatically create a mock here and include it in our mockspresso graph
@Mock lateinit var waterFilter: WaterFilter
```

[**@RealObject**](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso.annotation/-real-object/index.html): This is where the magic happens. Lateinit variables annotated with `@RealObject` will be created by mockspresso and injected with the dependencies included in the graph. Any dependencies required by the object that are not explicitly included in the mockspresso graph, will be automatically mocked. The resulting object also gets included in the graph, and can be a dependency of another `@RealObject`, enabling complex integration tests with simplified setups and zero boilerplate constructor calls.
```kotlin
// create a real SimpleCoffeeMaker for us to test
@RealObject lateinit var SimpleCoffeeMaker: SimpleCoffeeMaker

// create a real SimpleCoffeeMaker, but cast it as a generic CoffeeMaker
// both in this local variable an in the mockspresso graph
@RealObject(implementation = SimpleCoffeeMaker::class)
lateinit var coffeeMaker: CoffeeMaker

// apply a qualifier annotation to the object's binding in
// the mockspresso dependency graph
@RealObject @field:Named("simple")
lateinit var coffeeMaker: CoffeeMaker
```

### Builder methods
While mockspresso's annotation processing is usually the simplest way to add dependencies to the graph, we can also perform all the same operations using the methods (and extension methods) on [`Mockspresso.Builder`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/index.html). This can be useful if either a) we don't need/want a strong reference to a dependency in our test, or b) we want to make some common dependencies shareable by all tests.

**adding dependencies**

The dependency methods of Mockspresso.Builder allow us to add arbitrary objects to our dependency graph.

For example, if our class-under-test required an injected Rx Scheduler, we could bind the trampoline scheduler w/o retaining our own reference to it.
- Kotlin
  - `.dependencyOf<Scheduler> { Schedulers.trampoline() }`
    - The reified Scheduler type on the method dictates how the object will be bound in the dependency graph
- Java
  - `.dependency(Scheduler.class, Schedulers.trampoline())`
  - `.dependencyProvider(Scheduler.class, () -> Schedulers.trampoline())`

**declaring real objects**

When building complex integration tests with multiple real objects we rarely need to hold references to all of them directly once we've removed the need to call constructors. The Mockspresso.Builder allows us to declare that a specific dependency class/type should be created instead of mocked, and have its dependencies injected. If the dependency is bound in DI as an interface (or open class) we can specify the implementation we want as well.

For example, if we wanted to ensure our `WaterFilter` was a real object instead of a mock, we could apply the following...
- Kotlin
  - `.realClassOf<WaterFilter>()`
- Java
  - `.realObject(WaterFilter.class)`


If `WaterFilter` is an interface, we could apply the following...
- Kotlin
  - `.realImplOf<WaterFilter, WaterFilterImpl>()`
- Java
  - `.realObject(DependencyKey.of(WaterFilter.class), WaterFilterImpl.class)`



  **Reference the [`Mockspresso.Builder` java docs](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/index.html) for a complete list of builder methods.**
