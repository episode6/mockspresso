# ChangeLog

### v0.1.0 - unreleased
 - Add kotlin support/dependencies too all modules
 - **BREAKING** Replace jsr305 (`@Nullable`) with jetbrains annotations
 - Annotate interfaces null/not null for kotlin compatibility
 - **BREAKING** Remove retrolambda and target Java 8
 - **BREAKING** Hide internal entry-point using kotlin `internal` visibility that was formerly public but not intended for public use.
 - **BREAKING** Remove `Mockspresso.Builder.specialObjectMakers(List)` method. It's the only one of its kind and there is no good reason for it.
 - Added kotlin extension methods using reified types to reduce verbosity
     - `typeToken<T>()`: Create a `TypeToken<T>`
     - `dependencyKey<T>(Annotation? = null)`: Create a `DependencyKey<T>` with an optional qualifier
     - `Builder.dependencyOf<T>(Annotation? = null, ()->T?)`: Alias for `Builder.dependencyProvider<T>()`   
     - `Builder.realImpl<BIND, IMPL>(Annotation? = null)`: Alias for `Builder.realObject(DependencyKey<BIND>, TypeToken<IMPL>)`
     - `Builder.realClass<BIND_AND_IMPL>(Annotation? = null)`: Alias for `realImpl()` where `BIND` and `IMPL` are the same
 - Added kotlin convenience extension methods for built in plugins
     - `Builder.injectBySimpleConfig()`: Applies the simple injection configuration plugin
     - `Builder.injectByJavaxConfig()`: Applies the Javax injection configuration plugin
     - `Builder.mocksByMockito()`: Applies the mockito mocker config
     - `Builder.automaticFactories(vararg KClass<*>)`: Special object handling using `MockitoAutoFactoryMaker`

### v0.0.16 - March 24th, 2019
 - Added new method to Mockspresso api `<T> void Mockspresso.inject(T, TypeToken<T>)`. This acts as a workaround when injecting a pre-existing object that is generic and has injected TypeVariables defined.

### v0.0.15 - March 12th, 2019
 - Replaced old `TypeToken` implementation with guava's by way of https://github.com/episode6/mockspresso-reflect-guava. Adds about 1MB of bloat to the overall build, but now mockspresso doesn't fall over when parsing TypeVariable parameter.
 - **BREAKING**: The method `TypeToken.of(Field)` is no longer available. If you were using it for some reason, replace it with `TypeToken.of(field.getGenericType())`  

### v0.0.14 - June 28th, 2018
 - Add `Mockspresso.getDependency(DependencyKey)` to api
 - Add `@Dependency` annotation to api

### v0.0.13 - June 27th, 2018
 - introduce changelog
 - Remove `injector()` convenience method from main api
 - introduce `mockspresso-extend` library
 - refactor `mockspresso-quick` to build upon `mockspresso-extend`
 - enter the modern world of 2016 and apply retrolambda to the project
