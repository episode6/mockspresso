# mockspresso-extend
The `mockspresso-extend` libraray allows you create your own versions of the `Mockspresso`, `Mockspresso.Rule` and most-importantly `Mockspresso.Builder` apis. The mockspresso-quick library is actually built on top of `mockspresso-extend`, althought it's a more complex example as its intended for general use/bootstrapping.

**NOTE:** If your codebase is 100% kotlin, this library + these instructions are completely unnecessary. Everything shown here can more easily be accomplished by simply adding extension methods to `Mockspresso.Builder`. Sadly we don't all live in the land of kotlin and honey yet, so if you need to support keep supporting java, keep reading.

## Getting started
In this example we'll create a custom mockspresso extension for our imaginary project. Lets call it `PiedMocker`.

Our project is big with lots of modules, so we want to create a single module exposing all the test dependencies needed to test with `PiedMocker`...

**so lets start with a new build.gradle**
```groovy
apply plugin: 'java-library'

// note: '+' are used for example purposes only, normally you'd specify explicit versions
dependencies {
    api 'junit:junit:+
    api 'com.episode6.hackit.mockspresso:mockspresso-core:+'
    api 'com.episode6.hackit.mockspresso:mockspresso-extend:+'

    // we only mockito with mockito here
    implementation 'com.episode6.hackit.mockspresso:mockspresso-mockito:+'
    api 'org.mockito:mockito-core:+'

    // we also use dagger
    implementation 'com.episode6.hackit.mockspresso:mockspresso-dagger:+'
    api 'com.google.dagger:dagger'
}
```
**NOTE:** If you only have one module, you could simply add all the above deps under `testImplementation` and build out your extension directly in your `src/test` directory.

Next we'll define our extension as a collection of 3 interfaces (for `Mockspresso`, `Mockspresso.Rule`, and `Mockspresso.Builder`). However to do this, we will be extending generic interfaces from `mockspresso-extend` instead of the main api.

**Extend `MockspressoExtension`, `MockspressoExtension.Rule` and `MockspressoExtension.Builder`**
```java
public interface PiedMocker extends MockspressoExtension<PiedMocker.Builder> {

    interface Rule extends MockspressoExtension.Rule<PiedMocker.Builder> {}

    interface Builder extends MockspressoExtension.Builder<PiedMocker, PiedMocker.Rule, PiedMocker.Builder> {

        /* Custom builder methods go here */

        PiedMocker.Builder defaultConfig();
        PiedMocker.Builder daggerInjector();
        PiedMocker.Builder mockitoMocker();
    }
}
```
By leveraging the generic extension interfaces and referencing our own types in the Type Parameters, we avoid the need to override all our existing builder methods. By leveraging the abstract implementations in the next step, we'll also avoid implementing them.

**Create package-protected subclasses of `AbstractMockspressoExtension` and its inner `Rule` and `Builder` classes**
```java
// type parameters should point to the appropriate interface, but class should also
// implement its own appropriate interface directly
class PiedMockerImpl extends AbstactMockspressoExtension<PiedMocker.Builder> implements PiedMocker {

    // Mockspresso extension class must override constructor and buildUpon() method
    PiedMockerImpl(Mockspresso delegate) {
        super(delegate);
    }

    @Override
    public PiedMocker.Builder buildUpon() {
        // buildUpon the delegate and wrap with our builder impl below
        return new PiedMockerImpl.Builder(getDelegate().buildUpon());
    }

    static class Rule extends AbstactMockspressoExtension.Rule<PiedMocker.Builder> implements PiedMocker.Rule {

        // Mockspresso.Rule extension class must also override constructor and buildUpon() method
        Rule(Mockspresso.Rule delegate) {
            super(delegate);
        }

        @Override
        public PiedMocker.Builder buildUpon() {
            // buildUpon the delegate and wrap with our builder impl below
            return new PiedMockerImpl.Builder(getDelegate().buildUpon());
        }
    }

    static class Builder extends AbstactMockspressoExtension.Builder<PiedMocker, PiedMocker.Rule, PiedMocker.Builder> implements PiedMocker.Builder {

        // Mockspresso.Builder extension must override constructor, build() and buildRule() methods
        Rule(Mockspresso.Builder delegate) {
            super(delegate);
        }

        @Override
        public PiedMocker build() {
            // build the delegate and wrap with our outer class
            return new PiedMockerImpl(getDelegate().build());
        }

        @Override
        public PiedMocker.Rule buildRule() {
            // build the delegate rule and wrap with our rule impl above
            return new PiedMockerImpl.Rule(getDelegate().buildRule());
        }

        /*
         * Now we can implement our custom helper methods. We could also move this logic to
         * its own class if we want to keep the custom logic seperate from the boiler-plate.
         * But for this example, let's just do it here
         */

         @Override
         public PierMocker.Builder defaultConfig() {
            return daggerInjector()
                .mockitoMocker();
         }

         @Override
         public PierMocker.Builder daggerInjector() {
            return plugin(new DaggerMockspressoPlugin());
         }

         @Override
         public PierMocker.Builder mockitoMocker() {
            return plugin(new MockitoPlugin());
         }
    }
}
```
While there is obviously still some unavoidable boiler-plate here, most of it can be "automated" by a decent IDE, and its still considerably less effort then overriding every method in `Mockspresso.Builder` just to change the return type.

The last missing piece is a public way to create new `PiedMocker` instances from scratch.

**Add a static entry-point for new builders**
```java
public class BuildPiedMocker {

    public static PiedMocker.Builder with() {
        return new PiedMockerImpl.Builder(BuildMockspresso.with());
    }
}
```

In your tests you'll now be able to use `PiedMocker` in the same way as `Mockspresso` but with your own custom methods included in the builder pattern. For example...
```java
public class SomeTest {

    @Rule public final PiedMocker.Rule piedMocker = BuildPiedMocker.with()
        .defaultConfig()
        .buildRule();

    @Test
    public void someTestMethod() {
        // extension methods are available when building upon existing instances as well
        TestObject obj = piedMocker.buildUpon()
            .mockitoMocker()
            .dependency(String.class, "hello")
            .build()
            .create(TestObject.class);
    }
}
```