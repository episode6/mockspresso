# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests.

## What & Why?
Testing code is a pain in the ass. Mockspresso was created with the simple idea that if tests are easier to write and break less often, developers will hate them less, and write more of them.

Mockspresso creates your objects for you, letting you drop the constructors from your tests, while still giving you complete control over how your objects are created and what dependencies are provided/injected. Using the mocks declared in your test, mockspresso builds a [map of dependencies](#the-dependency-map) with which to inject your real objects. Mockspresso will auto-mock any dependencies that are undefined and can also intelligently map simple `Provider<>` / `Supplier<>` style interfaces to their actual dependencies (via [Special Object Handling](#special-object-handling)). The real objects that mockspresso creates are then also added to the dependency map, enabling complex integration tests as well as simple unit tests. The best part is, your tests wont break (by-default) just because a dependency is added to the class under test. After all, why should adding new functionality to a class, break the tests on its existing functionality?


## How?
Add the dependency on `mockspresso-core` as well as the depencies for your favorite mocking framework.
```groovy
repositories { jcenter() } // or mavenCentral()
dependencies {
    // mockspresso-core dependency
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-core:0.0.2'

    /* You'll also need specific dependencies for your mocking framework of choice */
    // for mockito
    testCompile 'org.mockito:mockito-core:2.+'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-mockito:0.0.2'

    // for easymock
    testCompile 'org.easymock:easymock:3.4'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-easymock:0.0.2'
}
```

Write your unit test
```java
public class CoffeeMakerTest {

    // Define your `Mockspresso` instance using a @Rule
    @Rule public final Mockspresso.Rule mockspresso = Mockspress.Builders.simple()
        .plugin(MockitoPlugin.getInstance()) // or EasyMockPlugin.getInstance()
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
  mockspresso = Mockspresso.Builders.simple()
      .plugin(MockitoPlugin.getInstance()) // or EasyMockPlugin.getInstance()
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

    @Rule public final Mockspresso.Rule mockspresso = Mockspresso.Builders.javaxInjection()
        .plugin(/* mocker plugin of preference*/)
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

    @Rule public final Mockspresso.Rule mockspresso = Mockspress.Builders.simple()
        .plugin(MockitoPlugin.getInstance())
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
In mockspresso, a `Plugin` is a simple class to package up multiple calls to a Mockspresso.Builder for related functionality. Mockspresso ships with a few plugins to get started.
- mockspresso-core
  - `SimpleInjectMockspressoPlugin` (usually accessed via `Mockspresso.Builders.simple()`): the most basic plugin we have. Applies the `SimpleInjectionConfig` so that mockspresso can create normal (non-DI) POJOs via their constructor. When creating real objects, the constructor with the fewest arguments will be chosen, and no post-processing will be applied.
  - `JavaxInjectMockspressoPlugin` (usually accessed via `Mockspresso.Builders.javaxInjection()`): create objects that are compatible with `javax.inject` dependency injection frameworks. When creating objects, mockspresso will only select a constructor annotated with @Inject OR (if none is found) a completely empty constructor. After the object is constructed, field/member injection is performed, followed by method injection. This plugin also applies the above-mentioned `ProviderMaker` for special handling of `javax.inject.Provider<>`
- mockspresso-mockito
  - `MockitoPlugin`: Applies the `MockitoMockerConfig` to provide compatibility with mockito.
- mockspresso-easymock
  - `EasyMockPlugin`: Applies the `EasyMockMockerConfig` to provide compatibility with easymock.

## License
MIT: https://github.com/episode6/mockspresso/blob/master/LICENSE