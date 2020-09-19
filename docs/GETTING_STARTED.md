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
Note that there is also a `build()` method available, but most unit tests will find `buildRule()` more convenient. See [Mockspresso on-the-fly](#mockspresso-on-the-fly) for details.

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

### Special Object Makers
Mockspresso special object makers allow us to customize the creation/mocking of objects based on their type and qualifier. They are also able to pull from mockspresso's dependency graph in order to map from one type/dependency to another. For example, the built-in [`automaticProviders()` plugin](javadocs/mockspresso-basic-plugins/mockspresso-basic-plugins/com.episode6.hackit.mockspresso.basic.plugin/com.episode6.hackit.mockspresso.-mockspresso.-builder/automatic-providers.html) leverages a special object maker to generate `javax.inject.Provider<T>`s that automatically map to a dependency of `T`.

Example...
```kotlin
class ClassUnderTest @Inject constructor(
  private val stringProvider: javax.inject.Provider<String>
) {
  fun string() = stringProvider.get()
}

class TestClass {
  @get:Rule val mockspresso = BuildMockspresso.withDefaults()
      .automaticProviders() // this isn't really necessary since it's included with javax injector
      .buildRule()

  @RealObject lateinit var objUnderTest: ClassUnderTest
  @Dependency val str = "hello"

  @Test fun verifyString() {
    assertThat(objUnderTest.string()).isEqualTo(str)
  }
}
```
Custom special object makers can be added via [`Mockspresso.Builder.specialObjectMaker()`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/special-object-maker.html)

### Test Resources

We've shown how, with the junit rule, mockspresso can automatically perform annotation processing on your test class to contribute dependencies to the dependency graph, and build realObjects from it. If we want to perform the same annotation processing on arbitrary objects other than the test class, we can do that using the [`Mockspresso.Build.testResources(Object)` method](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/test-resources.html). In addition to processing annotations for `@Dependency` `@RealObject` and `@Mock`, mockspresso will also find and execute methods annotated with junit's `@Before` and `@After` annotations (to avoid this, use [`Mockspresso.Build.testResourcesWithoutLifecycle()`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/test-resources-without-lifecycle.html) instead).

Example...
```kotlin
class SharedTestResources {
  @Mock lateinit var dep: ComplexDependency

  @Before fun setup() {
    whenever(dep.doThing).thenAnswer { /* complex return */ }
  }
}

class ActualTest {
  @get:Rule val mockspresso = BuildMockspresso.withDefaults()
      .testResources(SharedTestResources())
      .buildRule()

  @RealObject lateinit val complexDepWrapper: complexDepWrapper

  @Test fun testComplexDep() { /* etc... */ }
}
```
### Mockspresso on-the-fly
While a junit rule is the most common way to build mockspresso, instances can also be built and built-upon on-the-fly. When we use the  [`Mockspresso.Builder.build()` method](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/-builder/build.html) instead of buildRule, our [`Mockspresso`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/index.html) instance can be ready for use immediately, but we won't trigger the same automatic annotation processing (we can still leverage the [Test Resources](#test-resources) feature to process annotations on arbitrary objects).

For example, we could replace junit rule example with an on-the-fly instance...
```diff
 class CoffeeMakerHeaterTest {
-    @get:Rule val mockspresso = BuildMockspresso.withDefaults().buildRule()

     @Mock lateinit var heater: Heater
     @RealObject lateinit var coffeeMaker: CoffeeMaker
 
+    @Before fun setup() {
+      val mockspresso = BuildMockspresso.withDefaults()
+          .testResourcesWithoutLifecycle(this)
+          .build()
+    }   

    @Test fun testHeaterIsUser() {
        val coffee = coffeeMaker.brew()

        verify(heater).heat(any())
    }
 }
```

Once built, we can also create real objects and get dependencies programmatically as needed from the [`Mockspresso`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/index.html) instance.
```diff
 class CoffeeMakerHeaterTest {
-    @Mock
     lateinit var heater: Heater
-    @RealObject
     lateinit var coffeeMaker: CoffeeMaker
 
    @Before fun setup() {
      val mockspresso = BuildMockspresso.withDefaults()
          .testResourcesWithoutLifecycle(this)
          .build()
+     coffeeMaker = mockspresso.createNew()
+     heater = mockspresso.getDependencyOf()
    } 

    @Test fun testHeaterIsUser() {
        val coffee = coffeeMaker.brew()

        verify(heater).heat(any())
    }
 }
```

While we can't make changes to a `Mockspresso` instance once it's been built, we can [`buildUpon()`](javadocs/mockspresso-api/mockspresso-api/com.episode6.hackit.mockspresso/-mockspresso/build-upon.html) any mockspresso instance to create a new subgraph. We can override any dependencies in our sub-graph and create new realObjects with the updated deps. 

For example...
```kotlin
@Test fun testWithRealHeater() {
  val coffeeMaker = mockspresso.buildUpon()
      .realImplOf<Heater, WorkingHeater>() // override the Heater dependency
      .build()
      .createNew()

  val coffee = coffeeMaker.brew()

  assertThat(coffee).isHot()
}
```

The buildUpon method works both ways between on-the-fly mockspresso instance and rules (but only 1 rule can be generated in the chain).
