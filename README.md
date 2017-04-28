# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests.

## What & Why?
Testing code is a PITA. Writing code that does something is more engaging, more satisfying and more profitable than writing unit tests. But, as any seasoned engineer will confirm, testing is really really ridiculously important. Whether you're at a scale of 1 or 1,000, writing good unit tests leads to better code and fewer bugs, and test-driven-development can compound that even further.

Mockspresso aims to simplify this incredibly annoying, yet completely vital practice of writing unit tests (and integration tests) in java by handling the creation of real objects, and mapping the correct dependencies therein.


## How?
Add the dependency on `mockspresso-core` as well as the depencies for your favorite mocking framework.
```groovy
repositories { jcenter() } // or mavenCentral()
dependencies {
    // mockspresso-core dependency
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-core:0.0.2-SNAPSHOT'

    /* You'll also need specific dependencies for your mocking framework of choice */
    // for mockito
    testCompile 'org.mockito:mockito-core:2.+'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-mockito:0.0.2-SNAPSHOT'

    // for easymock
    testCompile 'org.easymock:easymock:3.4'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-easymock:0.0.2-SNAPSHOT'
}
```

Write your test
```java
public class CoffeeMakerTest {

    // Define your `Mockspresso` instance using a @Rule
    @Rule public final Mockspresso.Rule mockspresso = Mockspress.Builders.simple()
        .plugin(MockitoPlugin.getInstance()) // or EasyMockPlugin.getInstance()
        .buildRule();

    // Declare only the mocks you care about testing with
    @Mock Heater heater;

    // Declare an @RealObject, and mockspresso will create it for you
    @RealObject CoffeeMaker coffeeMakerUnderTest;

    @Test
    public void testCoffeeMaker() {
        // CoffeeMaker's dependencies are guaranteed to be non-null, even
        // if they aren't declared above.
        Coffee coffee = coffeeMakerUnderTest.brew();

        // verify that heater (a dependency of CoffeeMaker) was called (mockito example)
        verify(heater).heat(any(Water.class));
    }
}
```

### @RealObject annotation
You can think of the `@RealObject` annotation kind of like [Mockito's @InjectMocks](https://static.javadoc.io/org.mockito/mockito-core/2.7.19/org/mockito/InjectMocks.html) or [EasyMock's @TestSubject](http://easymock.org/api/org/easymock/TestSubject.html) annotations, but with super-powers.
- Declare a non-null (usually final) @RealObject field, and Mockspresso will add the value to its internal DependencyMap, and provide it as a dependency to other @RealObjects. This can be useful to inject dependencies that can't be mocked.
  - example: `final @RealObject String coffeeMakerName = "testCoffeeMaker";`
- Declare a null/empty @RealObject field, and Mockspresso will create the object for you. The object will be 'injected' with the other @Mocks and @RealObjects defined in your test class.
  - example: `@RealObject CoffeeMaker coffeeMakerUnderTest;`
- Declare multiple null/empty @RealObject fields to create an integration test with multiple real components working together.
- Declare the `implementation` property of @RealObject to specify a specific implementation be used to create the object.
  - example: define: `@RealObject(implementation = ElectricHeater.class) Heater mHeater;` and an `ElectricHeater` will be created and set in that field, but in mockspresso's DependencyMap, it will be mapped to the key for `Heater`, and provided as a dependency for any object that requires a generic Heater.


### Mockspresso on-the-fly
Mockspresso's functionality isn't limited to @Rules, instances of Mockspresso can be created or built-upon on the fly at runtime as well. For example, to build a functional equivalent of the above Mockspresso.Rule, you could implement the following setup method to your test
```java
private Mockspresso mockspresso;

@Before
public void setup() {
  mockspresso = Mockspresso.Builders.simple()
      .plugin(MockitoPlugin.getInstance()) // or EasyMockPlugin.getInstance()
      .testResourcesWithoutLifecycle(this) // scan 'this' for @Mocks and @RealObjects, but don't execute any of its lifecycle methods
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

        verify(t.mPump).pump();
        verify(mHeater).heat(t.mWater);
    }
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