## What & Why?
Mockspresso is an extensible auto-mocker for java and kotlin. 
The primary goal is to reduce the friction, boilerplate and barrier-to-entry writing unit-tests. 
A secondary goal is to be a vehicle to share common test code and dependencies.

## Installation
```groovy
def version = '{{ site.version }}'
repositories { jcenter() }
dependencies {

    // core module is required
    implementation "com.episode6.hackit.mockspresso:mockspresso-core:$version"
  
    // pick a support module for your mocking framework of choice
    implementation "com.episode6.hackit.mockspresso:mockspresso-mockito:$version"
    implementation "com.episode6.hackit.mockspresso:mockspresso-easymock:$version"
    implementation "com.episode6.hackit.mockspresso:mockspresso-mockito-powermock:$version"
    implementation "com.episode6.hackit.mockspresso:mockspresso-easymock-powermock:$version"
  
    // optionally include plugins for popular 3rd party libs
    implementation "com.episode6.hackit.mockspresso:mockspresso-dagger:$version"
    implementation "com.episode6.hackit.mockspresso:mockspresso-guava:$version"
}
```

## Basic JUnit Examples
**Kotlin Test**

```kotlin
// kotlin with mockito/mockito-kotlin
class CoffeeMakerHeaterTest {

    // setup mockspresso with junit rule
    @get:Rule val mockspresso = BuildMockspresso.with()
        .injectBySimpleConfig()
        .mockByMockito()
        .buildRule()
    
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

**Java Test**

```java
// java with mockito
public class CoffeeMakerHeaterTest {

    // setup mockspresso with junit rule
    @Rule public final Mockspresso.Rule mockspresso = BuildMockspresso.with()
        .plugin(injectBySimpleConfig())
        .plugin(mockByMockito())
        .buildRule();
    
    // declare only the mocks we need for our test
    @Mock Heater heater;

    // real object will be created for us
    @RealObject CoffeeMaker coffeeMaker;

    @Test public void testHeaterIsUser() {
        Coffee coffee = coffeeMaker.brew();

        verify(heater).heat(any());
    }
}
```

## JavaDocs
- [JavaDocs Root](javadocs/) 

## License
Mockspresso is licensed under the [MIT License](https://github.com/episode6/mockspresso/blob/master/LICENSE)
