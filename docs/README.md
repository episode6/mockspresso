## What & Why?
Mockspresso acts like a single-use DI graph for your java/kotlin unit and integration tests. `@Mock`s and `@Dependency`s are imported into the graph, and lateinit `@RealObject`s are constructed automatically (via reflection) and injected with your dependencies. Any dependencies that aren't explicitly supplied are mocked by default. RealObjects also get imported into the DI graph and can be combined to perform complex integration tests.

The primary goal is to reduce the friction, boilerplate, brittleness and barrier-to-entry when writing and updating unit-tests. Enabling you to focus on what matters...


```diff
 class CoffeeMakerHeaterTest {
+    @get:Rule val mockspresso = BuildMockspresso.withMyTestDefaults().buildRule()
 
+    @Dependency
     val heater: Heater = mock()
-    val filter: Filter = mock()
-    val timer: Timer = mock()
-    val analytics: Analytics = mock()
 
+    @RealObject
     lateinit var coffeeMaker: CoffeeMaker
 
-    @Before
-    fun setup() {
-      coffeeMaker = CoffeeMaker(heater, filter, timer, analytics)
-    }
-
     @Test fun testHeaterIsUser() {
         val coffee = coffeeMaker.brew()

         verify(heater).heat(any())
     }
 }
```

A secondary goal is to act as a vehicle to share common test code and utilities...

```diff
class CoffeeMakerHeaterTest {
     @get:Rule val mockspresso = BuildMockspresso.withMyTestDefaults()
+        .fakeFilter()  // named extension methods on the Builder allows for simple sharing
+        .trampoline()  // of test code/resources/plugins via discoverable composition
         .buildRule()
 }
```

## Installation
```groovy
def version = '0.1.0'
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

## More Resources
- [Getting Started](GETTING_STARTED.md)
- [Included Plugins](INCLUDED_PLUGINS.md)
- [JavaDocs Root](javadocs/)
- [ChangeLog](CHANGELOG.md)

## License
Mockspresso is licensed under the [MIT License](https://github.com/episode6/mockspresso/blob/master/LICENSE)
