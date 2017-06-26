# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests. (now with PowerMock support!)

## What & Why?
Testing code is a pain in the ass. Mockspresso was created with the simple idea that if tests are easier to write and break less often, developers will hate them less, and write more of them.

Mockspresso creates your objects for you, letting you drop the constructors from your tests, while still giving you complete control over how your objects are created and what dependencies are provided/injected. Using the mocks declared in your test, mockspresso builds a [map of dependencies](#the-dependency-map) with which to inject your real objects. Mockspresso will auto-mock any dependencies that are undefined and can also intelligently map simple `Provider<>` / `Supplier<>` style interfaces to their actual dependencies (via [Special Object Handling](#special-object-handling)). The real objects that mockspresso creates are then also added to the dependency map, enabling complex integration tests as well as simple unit tests. The best part is, your tests wont break (by-default) just because a dependency is added to the class under test. After all, why should adding new functionality to a class, break the tests on its existing functionality?


## How?
Add the dependency on `mockspresso-core` as well as the depencies for your favorite mocking framework.
```groovy
repositories { maven { url "https://oss.sonatype.org/content/repositories/snapshots/" } }
dependencies {
    // mockspresso-core dependency
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-core:0.0.8-SNAPSHOT'

    /* You'll also need the dependencies for your mocking framework of choice */
    // for mockito
    testCompile 'org.mockito:mockito-core:2.+'

    // for easymock
    testCompile 'org.easymock:easymock:3.4'
}
```

Write your unit test
```java
public class CoffeeMakerTest {

    // Define your `Mockspresso` instance using a @Rule
    @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
        .injector().simple() // use constructor-based injection for real objects
        .mocker().mockito() // or mocker().easyMock()
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
  mockspresso = BuildMockspresso.with()
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

    @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
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

    @Before
    public void setup(Mockspresso mockspresso) { // Mockspresso param is optional
      when(mPump.pump()).thenReturn(mWater);
    }
}

// Test using shared resources
public class CoffeeMakerTest {

    final SharedCoffeeMakerTestResource t = new SharedCoffeeMakerTestResource();

    @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
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
Multiple bits of mockspresso functionality can be packaged into `MockspressoPlugin`s. Mockspresso ships with the following built-in plugins accessible via methods in Mockspresso.Builder. Some of these plugins will require extra dependencies to function (mockspresso declares them as optional dependencies to simplify the end-user implementation).

- **Injectors**: An injector is a required component of Mockspresso that dictates how real objects are created.
  - **`injector().simple()`**: Our most basic injector plugin. Creates POJOs using their shortest constructor and does no post-processing or field injection

  - **`injector().javax()`**: Creates objects that are compatible with `javax.inject` dependency injection frameworks. When creating objects, mockspresso will only select a constructor annotated with @Inject OR (if none is found) a completely empty constructor. After the object is constructed, field/member injection is performed, followed by method injection. This plugin also applies the above-mentioned `ProviderMaker` for special handling of `javax.inject.Provider<>`

  - **`injector().dagger()`**: Builds upon the javax injector and adds special object handling for `dagger.Lazy<>`
    - Requires dependency on `com.google.dagger:dagger` or `com.squareup.dagger:dagger`

- **Mockers**: A mocker is also a required component of mockspresso, as it dictates how generic mocks are created and which mock annotations should be processed by the dependency map.
  - **`mocker().mockito()`**: Use Mockito for mockspresso mocks.
    - Requires dependency: `org.mockito:mockito-core:2.+`

  - **`mocker().mockitoWithPowerMock()`**: Use Mockito with PowerMock for mockspresso mocks.
    - Requires dependencies from mockito(), `org.powermock:powermock-api-mockito2` and `org.powermock:powermock-module-junit4`. Also requires your test be run with the `PowerMockRunner`.

  - **`mocker().mockitoWithPowerMockRule()`**: Similar to mockitoWithPowerMock(), but also applies a `PowerMockRule` as an outerRule to mockspresso, thereby removing the requirement to use `PowerMockRunner`.
    - Requires dependencies from mockitoWithPowerMock() as well as `org.powermock:powermock-module-junit4-rule` and `org.powermock:powermock-classloading-xstream`.

  - **`mocker().easyMock()`**: Uses EasyMock for mockspresso mocks.
    - Requires dependency: `org.easymock:easymock:3.4`

  - **`mocker().easyMockWithPowerMock()`**: Use EasyMock with PowerMock for mockspresso mocks.
    - Requires dependencies from mockito(), `org.powermock:powermock-api-easymock` and `org.powermock:powermock-module-junit4`. Also requires your test be run with the `PowerMockRunner`.

  - **`mocker().easyMockWithPowerMockRule()`**: Similar to easyMockWithPowerMock(), but also applies a `PowerMockRule` as an outerRule to mockspresso, thereby removing the requirement to use `PowerMockRunner`.
    - Requires dependencies from easyMockWithPowerMock(), `org.powermock:powermock-module-junit4-rule` and `org.powermock:powermock-classloading-xstream`.

- **Plugins**: Built-in plugins that don't fit into mocker or injector categories. Usually collections of special object makers.
  - **`plugin().guava()`**: Special object handling for some of guava's interfaces (currently supports Supplier and ListenableFuture).
    - Requires dependency on `com.google.guava:guava`

  - **`plugin().automaticFactories(Class<?> factoryClasses)`**: Special object handling for your project's factory classes. Factory classes will be automatically mocked to return the underlying mockspresso binding from their methods that return objects. This is just link the automatic handling for `Provider`s and `Lazy`s, but can be applied to any class in your project (including most generics).
    - Requires dependency: `org.mockito:mockito-core:2.+` (or `mockito-inline` to support final factories). Does NOT require using the mockito mocker, only that mockito be available on the classpath.


## License
MIT: https://github.com/episode6/mockspresso/blob/master/LICENSE