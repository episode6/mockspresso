# mockspresso
An extensible auto-mocker for java, designed to simplify your unit tests.

### What & Why?
Testing code is a PITA. Writing code that does something is more engaging, more satisfying and more profitable than writing unit tests. But, as any seasoned engineer will confirm, testing is really really ridiculously important. Whether you're at a scale of 1 or 1,000, writing good unit tests leads to better code and fewer bugs, and test-driven-development can compound that even further.

Mockspresso aims to simplify this incredibly annoying, yet completely vital practice of writing unit tests (and integration tests) in java by handling the creation of real objects, and mapping the correct dependencies therein. If you've ever used [Mockito's @InjectMocks](https://static.javadoc.io/org.mockito/mockito-core/2.7.19/org/mockito/InjectMocks.html) or [EasyMock's @TestSubject](http://easymock.org/api/org/easymock/TestSubject.html) annotations, then you can think of Mockspresso as a more advanced replacement with a few key differences.

- Mockspresso is mock-agnostic, meaning you can use it with your favorite mocking framework. We currently support [Mockito](http://site.mockito.org/) and [EasyMock](http://easymock.org/) out of the box.
- Mockspresso can scan your test for annotated fields and add them to its internal dependency map, just like Mockito and EasyMock's injectMocks functions.
- Mockspresso can create real objects, and "inject" mocks and other real dependencies into them.
- Objects that mockspresso creates are guaranteed to receive non-null dependencies. If mockspresso encounters a dependency that it doesn't have a mapped entry for, a new mock will be created and supplied.
- Mockspresso plugins can add special handling for "special" object types
  - Example: The built in JavaxInjectMockspressoPlugin adds special object handling for `javax.inject.Provider<T>`, so if your real object depends on `Provider<Foo>`, you only have to mock a `Foo` object, and it will be automatically supplied to the Provider.

```java
@RunWith(JUnit4.class)
public class CoffeeMakerTest {

    // The simplest way to initialize mockspresso is via a Mockspresso.Rule
    @Rule public final Mockspresso.Rule mockspresso = Mockspress.Builders.simple()
        .plugin(/* MockitoPlugin or EasyMockPlugin */)
        .buildRule();

    // The MockitoPlugin or EasyMockPlugin will take care of initializing
    // your mocks, no need to explicitly init them.
    @Mock Heater heater;

    // This will be a real object, created by mockspresso.
    // If CoffeeMaker depends on Heater, the above mock will be provided.
    @RealObject CoffeeMaker coffeeMaker;

    @Test
    public void testCoffeeMaker() {
        Coffee coffee = coffeeMaker.brew();

        // verify heater.heat()
    }
}
```