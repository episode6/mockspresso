# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests.

`It's like dependency injection for your tests.`

## v0.1.0-beta2
Mockspresso v0.1.0 represents a complete shift in focus from Java to Kotlin. We've added extension methods for all built in plugins as well as inline functions with reified types for any method that uses a `TypeToken`.
 
Check the [ChangeLog](CHANGELOG.md) for details.

## Quickstart
- [Updated Quickstart Guide](docs/README.md)

---
## Everything below is totally out of date and will be moved/updated in the next release
---

## Usage

First you define an instance of `Mockspresso` with an injector and a mocker. In most cases you'll want to do this via a `Mockspresso.Rule`

##### (1) mockspresso-core
```java
public class CoffeeMakerTest {
    @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
        .injector(new SimpleInjectionConfig()) // or new JavaxInjectionConfig()
        .mocker(new MockitoMockerConfig()) // or new EasyMockMockerConfig()
        .buildRule();
```

##### (2) mockspresso-quick
```java
public class CoffeeMakerTest {
    @Rule public final QuickMockspresso.Rule mockspresso = BuildQuickMockspresso.with()
        .injector().simple() // or injector().javax()
        .mocker().mockito() // or mocker().easyMock()
        .buildRule();
```

**NOTE**: for the rest of these examples we'll stick with the `mockspresso-quick` api.

Write your unit test
```java
public class CoffeeMakerTest {

    // Define your `Mockspresso` instance using a @Rule
    @Rule public final QuickMockspresso.Rule mockspresso = BuildQuickMockspresso.with()
        .injector().simple() // use constructor-based injection for real objects
        .mocker().mockito()
        .buildRule();

    // Declare only the mocks you care about
    @Mock Heater heater;

    // Declare a @RealObject and mockspresso will create it for you.
    // The CoffeeMaker will be injected with the Heater declared above,
    // and any other dependencies will be automatically mocked.
    @RealObject CoffeeMaker coffeeMakerUnderTest;

    @Test
    public void testCoffeeMaker() {
        // CoffeeMaker's dependencies are guaranteed to be non-null, even
        // if they aren't declared on this test.
        Coffee coffee = coffeeMakerUnderTest.brew();

        // verify that heater (a dependency of CoffeeMaker) was called (mockito example)
        verify(heater).heat(any(Water.class));
    }
}
```

### Mockspresso on-the-fly
Mockspresso's functionality isn't limited to @Rules, instances of Mockspresso can be created or built-upon on the fly at runtime as well. For example, to build a functional equivalent of our first Mockspresso.Rule, you could implement the following setup method to your test
```java
private Mockspresso mockspresso;

@Before
public void setup() {
  mockspresso = BuildQuickMockspresso.with()
      .injector().simple()
      .mocker().mockito()
      .testResourcesWithoutLifecycle(this) // scan 'this' for @Mocks and @RealObjects, but
                                           // don't execute any of its lifecycle methods
      .build(); // use build() instead of buildRule() for a raw instance of Mockspresso
}
```

You can also create real objects at runtime, using `Mockspresso.create()`
```java
@Test
public void testCoffeeMaker() {
    CoffeeMaker realCoffeeMaker = mockspresso.create(CoffeeMaker.class);

    // test realCoffeeMaker...
}
```

Or inject components after creation, using `Mockspresso.inject()`
```java
@Test
public void testCoffeeMaker() {
    CoffeeMakerView androidCoffeeMakerView = new CoffeeMakerView(context);
    mockspresso.inject(androidCoffeeMakerView);

    // test androidCoffeeMakerView...
}

@Test
public void testGenericCoffeeMaker() {
    GenericCoffeeMaker<FilterType> injectableGeneric = new GenericCoffeeMaker<>();
    
    // include the TypeToken for classes with injectable generic typeVariables
    mockspresso.inject(injectableGeneric, new TypeToken<GenericCoffeeMaker<FilterType>>() {});
}
```

You can buildUpon existing mockspresso instances (if some tests require different properties/dependencies).
```java
@Test
public void testWithRealHeater() {
    RealHeater realHeater = new RealHeater();

    CoffeeMaker coffeeMakerWithRealHeaterAndPump = mockspresso.buildUpon()
        .dependency(Heater.class, realHeater) // apply a specific instance of a Heater dependency.
        .realObject(Pump.class) // tell mockspresso to create a real Pump instead of mocking it.
        .build() // builds the new mockspresso instance
        .create(CoffeeMaker.class);

    // test coffeeMakerWithRealHeaterAndPump...
}
```

### The Dependency Map
Mockspresso tries to adhere to principles set fourth by the javax.inject package and compatible DI frameworks. As such, each dependency in our map is keyed to the `Type` of the dependency and an optional qualifier `Annotation` (any annotation that itself is annotated with `javax.inject.Qualifier`). When mockspresso scans your test or testResource for fields annotated with @Mock or @RealObject, it also respects the Qualifier annotations on these fields. While you can override anything in the dependency map by using `Mockspresso.buildUpon()` to create a new mockspresso instance, defining multiple objects/mocks with the same dependency key in the same mockspresso instance will result in a `RepeatedDependencyDefinedException`. For example...
```java
// These represent three distinct dependency keys. heater2 and heater3 will only be supplied to
// real objects if those objects specify '@Named Heater' and '@Named("somename") Heater'
// (respectively) as constructor parameters (or field params if field injection is enabled).
// Similarly, heater1 will only be supplied to a real object if that object does not specify a
// qualifier annotation on its Heater dependency.
@Mock Heater heater1;
@Mock @Named Heater heater2; // @Named is a qualifier annotation provided by javax.inject
@Mock @Named("somename") Heater heater2;

// Uncommenting the line below would trigger a RepeatedDependencyDefinedException because
// we've already mapped Heater's dependency key to a mock above.
// @RealObject Heater realHeater;
```
To allow for multiple mocks / objects of the same type to be created in the same mockspresso instance, see [The @Unmapped Annotation](#unmapped-annotation), and skip the dependency map entirely.

### @RealObject Annotation
You can think of the `@RealObject` annotation kind of like [Mockito's @InjectMocks](https://static.javadoc.io/org.mockito/mockito-core/2.7.19/org/mockito/InjectMocks.html) or [EasyMock's @TestSubject](http://easymock.org/api/org/easymock/TestSubject.html) annotations, but with super-powers.
```java
// Declare a null @RealObject and mockspresso will construct it for you.
// The object will be 'injected' with other @Mocks and @RealObjects defined
// in you test class and shared resources.
@RealObject CoffeeMaker coffeeMakerUnderTest;

// Specify the implementation field to inform mockspresso which implementation
// to create of a given class/interface. In this example an ElectricHeater will be
// created, but it will be bound to the dependency key for Heater, and supplied to
// the CoffeeMaker above (assuming it requires Heater as a dependency).
@RealObject(implementation = ElectricHeater.class) Heater mHeater;

// Declare a non-null @RealObject, and mockspresso will simply add it to it's dependency map without
// modifying it. This can be useful for unmockable dependencies like primitives and Strings.
final @RealObject @Named("coffee_maker_name") String mCoffeeMakerName = "Test Coffee Maker";

// Combine multiple @RealObjects in one test (as we have above) to create an integration test without
// ever touching a constructor.
```

### @Dependency Annotation
Use the `@Dependency` annotation when you want to explicitly add an instance of an object to mockspresso's dependency map. You can also specify a different class to bind the object to...
```java
// Bind a simple implementation to an interface
@Dependency final Scheduler scheduler = Schedulers.trampoline();

// Bind a concrete implementation as an interface
@Dependency(bindAs = Scheduler.class) final TestScheduler testScheduler = new TestScheduler();

// Qualifier annotations are also valid and respected as part of the binding key
@ForMainThread
@Dependency(bindAs = Scheduler.class) final TestScheduler testScheduler = new TestScheduler();
```

### Special Object Handling
A key feature of mockspresso's dependency mapping is its concept of "special objects." A special object is simply defined as an object type that should not be mocked by default. One can add `SpecialObjectMaker`s via the `Mockspresso.Builder.specialObjectMaker()` method (or via a plugin).

Example: The built-in `JavaxInjectMockspressoPlugin` includes a SpecialObjectMaker for `javax.inject.Provider<>` that automatically fetches the underlying dependency when get() is called, allowing you to ignore Providers in your test entirely...
```java
// Given a class with a dependency on a Provider<>...
public class ObjectUnderTest {
    private final javax.inject.Provider<MyDependency> mDependencyProvider;

    // constructor, etc...

    public void touchDependency() {
        mDependencyProvider.get().touchMe();
    }
}

// You can write a test that simply pretends the Provider<> doesn't exist
public class MyTest {

    @Rule public final QuickMockspresso.Rule mockspresso = BuildQuickMockspresso.with()
        .injector().javax() // use the built-in JavaxInjectMockspressoPlugin
        .mocker().mockito()
        .buildRule();

    // this dependency will automatically be fetched by Provider<MyDependency>
    @Mock MyDependency mDependency;

    @RealObject ObjectUnderTest mObjectUnderTest

    @Test
    public void testTouch() {
        mObjectUnderTest.touchDependency();

        verify(mDependency).touchMe();
    }
}
```

### Shared Test Resources
Mockspresso makes it easy to share resources across multiple tests. A shared test resource is any class with annotated @Mock or @RealObject fields, and/or annotated @Before and @After methods for setup and teardown. The fields will be scanned and mixed into mockspresso's dependency map (along with the fields on your test).
```java
// Example Shared Resource
public class SharedCoffeeMakerTestResource {

    @Mock Pump mPump;
    @Mock Water mWater;

    // Usually you'd define real objects on the test itself,
    // but they can be shared as well
    @RealObject CoffeeMaker mCofferMaker;

    /**
     * A mockspresso instance may optionally be included in methods annotated with
     * org.junit.Before, but only the com.episode6.hackit.mockspresso.Mockspresso
     * interface may be present in the method signature. To use QuickMockspresso or
     * another extension, the reference must be wrapped manually.
     *
     * i.e.
     *
     * QuickMockspresso quickMockspresso = BuildQuickMockspress.upon(mockspresso)
     *     .plugin().guava()
     *     .build();
     */
    @Before
    public void setup(Mockspresso mockspresso) { // Mockspresso param is optional
      when(mPump.pump()).thenReturn(mWater);
    }
}

// Test using shared resources
public class CoffeeMakerTest {

    final SharedCoffeeMakerTestResource t = new SharedCoffeeMakerTestResource();

    @Rule public final Mockspresso.Rule mockspresso = QuickBuildMockspresso.with()
        .injector().simple()
        .mocker().mockito()
        .testResources(t) // resources from t will be mixed in with resources from this test
        .buildRule();

    @Mock Heater mHeater;

    @Test
    public void testSomething() {
        Coffee coffee = t.mCoffeeMaker.brew();

        // verify both the shared mock and our own mock were called.
        verify(t.mPump).pump();
        verify(mHeater).heat(t.mWater);
    }
}
```


### @Unmapped Annotation
Sometimes you may need to mock multiple instances of the same class or create multiple instances of a real object. Using qualifier annotations for these instances only makes sense if your code uses them as well, so we've included the `@Unmapped` annotation to indicate that a @Mock or @RealObject should be excluded from the dependency map, and will be handled manually in the test. For example...

 ```java
 // Without @Unmapped, this would throw a RepeatedDependencyDefinedException
 @Unmapped @Mock CoffeeGrounds coffeeGrounds1
 @Unmapped @Mock CoffeeGrounds coffeeGrounds2

 // This provider will be bound to the dependency map and passed
 // into the coffeeMaker below.
 @Mock Provider<CoffeeGrounds> coffeeGroundsProvider;

 @RealObject CoffeeMaker coffeeMaker;

 @Before
 public void setup() {
     when(coffeeGroundsProvider.get())
        .thenReturn(coffeeGrounds1)
        .thenReturn(coffeeGrounds2);
 }
 ```


### Plugins
Multiple bits of mockspresso functionality can be packaged into `MockspressoPlugin`s. Mockspresso ships with the following built-in plugins, and they are all accessible either directly or via the `mockspresso-quick` api. Plugins are split into 3 categories...

**Injectors**: An injector is a required component of Mockspresso that dictates how real objects are created.

`mockspresso-quick` | manual usage | dependencies | description
------------------------- | ------------ | ------------ | -----------
`.injector().simple()` | `.plugin(new SimpleInjectMockspressoPlugin())` | core | Our most basic injector plugin. Creates POJOs using their shortest constructor and does no post-processing or field injection.
`.injector().javax()` | `.plugin(new JavaxInjectMockspressoPlugin())` | core | Creates objects that are compatible with `javax.inject` dependency injection frameworks. When creating objects, mockspresso will only select a constructor annotated with @Inject OR (if none is found) a completely empty constructor. After the object is constructed, field/member injection is performed, followed by method injection. This plugin also applies the above-mentioned `ProviderMaker` for special handling of `javax.inject.Provider<>`
`.injector().dagger()` | `.plugin(new DaggerMockspressoPlugin())` | **`:mockspresso-dagger`**, `com.google.dagger:dagger` | Builds upon the javax injector and adds special object handling for `dagger.Lazy<>`.

**Mockers**: A mocker is also a required component of mockspresso, as it dictates how generic mocks are created and which mock annotations should be processed by the dependency map.

`mockspresso-quick` usage | manual usage | dependencies | description
------------------------- | ------------ | ------------ | -----------
`.mocker().mockito()` | `.plugin(new MockitoPlugin())` | **`:mockspresso-mockito`**, `org.mockito:mockito-core:2.+` | Use Mockito for mockspresso mocks.
`.mocker().mockitoWithPowerMock()` | `.plugin(new PowerMockitoPlugin())` | **`:mockspresso-mockito-powermock`**, `org.mockito:mockito-core:2.+`, `org.powermock:powermock-api-mockito2`, `org.powermock:powermock-module-junit4`  | Use Mockito with PowerMock for mockspresso mocks. Requires your test be run with the `PowerMockRunner`.
`.mocker().mockitoWithPowerMockRule()` | `.plugin(new PowerMockitoRulePlugin())` | **`:mockspresso-mockito-powermock`**, `org.mockito:mockito-core:2.+`, `org.powermock:powermock-api-mockito2`, `org.powermock:powermock-module-junit4`, `org.powermock:powermock-module-junit4-rule`, `org.powermock:powermock-classloading-xstream` | Similar to mockitoWithPowerMock(), but also applies a `PowerMockRule` as an outerRule to mockspresso, thereby removing the requirement to use `PowerMockRunner`.
`.mocker().easyMock()` | `.plugin(new EasyMockPlugin())` | **`:mockspresso-easymock`**, `org.easymock:easymock:3.4+` | Use EasyMock for mockspresso mocks.
`.mocker().easyMockWithPowerMock()` | `.plugin(new EasyPowerMockPlugin())` | **`:mockspresso-easymock-powermock`**, `org.easymock:easymock:3.4+`, `org.powermock:powermock-api-easymock`, `org.powermock:powermock-module-junit4` | Use EasyMock with PowerMock for mockspresso mocks. Requires your test be run with the `PowerMockRunner`.
`.mocker().easyMockWithPowerMockRule()` | `.plugin(new EasyPowerMockRulePlugin())` | **`:mockspresso-easymock-powermock`**, `org.easymock:easymock:3.4+`, `org.powermock:powermock-api-easymock`, `org.powermock:powermock-module-junit4`, `org.powermock:powermock-module-junit4-rule`, `org.powermock:powermock-classloading-xstream` | Similar to easyMockWithPowerMock(), but also applies a `PowerMockRule` as an outerRule to mockspresso, thereby removing the requirement to use `PowerMockRunner`.

**Plugins**: Built-in plugins that don't fit into mocker or injector categories. Usually collections of special object makers.

`mockspresso-quick` usage | manual usage | dependencies | description
------------------------- | ------------ | ------------ | -----------
`.plugin().guava()` | `.plugin(new GuavaMockspressoPlugin())` | **`:mockspresso-guava`**, `com.google.guava:guava` | Special object handling for some of guava's interfaces (currently supports Supplier and ListenableFuture).
`.plugin().automaticFactories( Class<?>... factoryClasses)` | `.specialObjectMaker(MockitoAutoFactoryMaker.create( Class<?>... factoryClasses))` | **`:mockspresso-mockito`**, `org.mockito:mockito-core:2.+` | Special object handling for your project's factory classes. Factory classes will be automatically mocked to return the underlying mockspresso binding from their methods that return objects. This is just like the automatic handling for `Provider`s and `Lazy`s, but can be applied to any class in your project (including most generics). Does NOT require using the mockito mocker, only that mockito be available on the classpath.


## License
MIT: https://github.com/episode6/mockspresso/blob/master/LICENSE
