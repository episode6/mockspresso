# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests.

### What & Why?
Testing code is a PITA. Writing code that does something is more engaging, more satisfying and more profitable than writing unit tests. But, as any seasoned engineer will confirm, testing is really really ridiculously important. Whether you're at a scale of 1 or 1,000, writing good unit tests leads to better code and fewer bugs, and test-driven-development can compound that even further.

Mockspresso aims to simplify this incredibly annoying, yet completely vital practice of writing unit tests (and integration tests) in java by handling the creation of real objects, and mapping the correct dependencies therein.


### How?
Add the dependency on `mockspresso-core` as well as the depencies for your favorite mocking framework.
```groovy
repositories { jcenter() } // or mavenCentral()
dependencies {
    // mockspresso-core dependency
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-core:0.0.1-SNAPSHOT'

    /* You'll also need specific dependencies for your mocking framework of choice */
    // for mockito
    testCompile 'org.mockito:mockito-core:2.+'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-mockito:0.0.1-SNAPSHOT'

    // for easymock
    testCompile 'org.easymock:easymock:3.4'
    testCompile 'com.episode6.hackit.mockspresso:mockspresso-easymock:0.0.1-SNAPSHOT'
}
```

Define your `Mockspresso` instance using a @Rule
```java
@Rule public final Mockspresso.Rule mockspresso = Mockspress.Builders.simple()
    .plugin(MockitoPlugin.getInstance()) // or EasyMockPlugin.getInstance()
    .buildRule();
```

Declare the mocks you care about, and the real objects mockspresso should create
```java
// mockspresso will take care of initializing your mocks
@Mock Heater heater;

// the @RealObject annotation instructs mockspresso to create this object
// and provide all required dependencies to it.
@RealObject CoffeeMaker coffeeMakerUnderTest;
```

Write your test
```java
@Test
public void testCoffeeMaker() {
    // CoffeeMaker's dependencies are guaranteed to be non-null
    Coffee coffee = coffeeMakerUnderTest.brew();

    // verify that heater (a dependency of CoffeeMaker) was called (mockito example)
    verify(heater).heat(any(Water.class));
}
```

### @RealObject
You can think of the `@RealObject` annotation kind of like [Mockito's @InjectMocks](https://static.javadoc.io/org.mockito/mockito-core/2.7.19/org/mockito/InjectMocks.html) or [EasyMock's @TestSubject](http://easymock.org/api/org/easymock/TestSubject.html) annotations, but with super-powers.
- Declare a non-null (usually final) @RealObject field, and Mockspresso will add the value to its internal DependencyMap, and provide it as a dependency to other @RealObjects. This can be useful to inject dependencies that can't be mocked.
  - example: `final @RealObject String coffeeMakerName = "testCoffeeMaker";`
- Declare a null/empty @RealObject field, and Mockspresso will create the object for you. The object will be 'injected' with the other @Mocks and @RealObjects defined in your test class.
  - example: `@RealObject CoffeeMaker coffeeMakerUnderTest;`
- Declare multiple null/empty @RealObject fields to create an integration test with multiple real components working together.
- Declare the `implementation` property of @RealObject to specify a specific implementation be used to create the object.
  - example: If you define the following field on your test, `@RealObject(implementation = ElectricHeater.class) Heater mHeater;` An `ElectricHeater` will be created and set in that field, but in mockspresso's DependencyMap, it will be mapped to the key for `Heater`, and provided as a dependency for any object that required a generic Heater.

### Dependency Keys
To supply the dependencies for real objects, mockspresso constructs an internal DependencyMap, and translates every dependency into a `DependencyKey`. These DependencyKeys are made up two components, (a) the `Type` of the dependency and (b) an optional "Qualifier" annotation (applied to the dependent field or constructor param). A Qualifier annotation is defined as any annotation that contains the `javax.inject.Qualifier` annotation (example: `javax.inject.Named`).
- When you define any @Mock or @RealObject on your test, you are mapping the object to a DependencyKey matching that field. You may add a single Qualifier annotation to the @Mock or @RealObject. I.e. If your CoffeeMaker depends on `@Named("forCoffeeMaker") Heater heater;`
  - then you'll want to include the the @Named annotation on your mock: `@Mock @Named("forCoffeeMaker") Heater mHeater`
  - or on your real object: `@RealObject(implementation = ElectricHeater.class) @Named("forCoffeeMaker") Heater mHeater;`

## License
MIT: https://github.com/episode6/mockspresso/blob/master/LICENSE