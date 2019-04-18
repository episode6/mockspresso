# ChangeLog


### v0.1.0-beta3 - unreleased


### v0.1.0-beta2 - April 17th, 2019
 - **DEPRECATED** All concrete implemenations of `MockspressoPlugin` in favor of their kotlin extension and `*JavaSupport` counterparts. These classes are no longer used and will be deleted in a future version.
 - **DEPRECATED** All other concrete classes in our plugin modules (excluding the new `*JavaSupport` objects). These classes will be made final and either kotlin-internal or package-protected in a future version.


### v0.1.0-beta1 - April 7th, 2019
 - Add kotlin support/dependencies too all modules
 - **BREAKING** Replace jsr305 (`@Nullable`) with jetbrains annotations
 - Annotate interfaces null/not null for kotlin compatibility
 - **BREAKING** Remove retrolambda and target Java 8
 - **BREAKING** Hide internal entry-point using kotlin `internal` visibility that was formerly public but not intended for public use.
 - **BREAKING** Remove `Mockspresso.Builder.specialObjectMakers(List)` method. It's the only one of its kind and there is no good reason for it.
 - **BREAKING** Remove deprecated `QuickBuildMockspresso` entry-point
 - Added kotlin extension methods using reified types to reduce verbosity
     - **:mockspresso-reflect** module
     - `typeToken<T>()`: Create a TypeToken<T>
     - `dependencyKey<T>(Annotation? = null)`: Create a DependencyKey<T> with an optional qualifier
     - **:mockspresso-api** module
     - `Builder.dependencyOf<T>(Annotation? = null, ()->T?)`: Alias for Builder.dependencyProvider<T>()   
     - `Builder.realImpl<BIND, IMPL>(Annotation? = null)`: Alias for Builder.realObject(DependencyKey<BIND>, TypeToken<IMPL>)
     - `Builder.realClass<BIND_AND_IMPL>(Annotation? = null)`: Alias for realImpl() where BIND and IMPL are the same
     - `Mockspresso.createNew<T>()`: Alias for Mockspresso.create()
     - `Mockspresso.injectType<T>(T)`: Alias for Mockspresso.inject(T, TypeToken<T>) with support for generic type parameter dependencies
     - `Mockspresso.getDependencyOf<T>(Annotation? = null)`: Alias for Mockspresso.getDependency(DependencyKey)
 - Added kotlin convenience extension methods for built in plugins
     - **:mockspresso-basic-plugins** module
     - `Builder.injectBySimpleConfig()`: Applies the simple injection configuration plugin
     - `Builder.injectByJavaxConfig()`: Applies the Javax injection configuration plugin
     - `Builder.automaticProviders()`: Adds special object handling for javax Providers
     - **:mockspresso-dagger** module
     - `Builder.injectByDaggerConfig()`: Applies the dagger injection configuration plugin
     - `Builder.automaticLazies()`: Adds special object handling for dagger Lazies
     - **:mockspresso-mockito** module
     - `Builder.mockByMockito()`: Applies the mockito mocker config
     - `Builder.automaticFactories(vararg KClass<*>)`: Special object handling using MockitoAutoFactoryMaker
     - **:mockspresso-mockito-powermock** module
     - `Builder.mockByPowerMockito()`: Applies the power mockito mocker config
     - `Builder.mockByPowerMockitoRule()`: Applies the power mockito + junit rule mocker config
     - **:mockspresso-easymock** module
     - `Builder.mockByEasyMock()`: Applies the easy mock mocker config
     - **:mockspresso-easymock-powermock** module
     - `Builder.mockByPowerMock()`: Applies the power mock mocker config
     - `Builder.mockByPowerMockRule()`: Applies the power mock + junit rule mocker config     
     - **:mockspresso-guava** module
     - `Builder.automaticListenableFutures()`: Adds special object handling for ListenableFutures
     - `Builder.automaticSuppliers()`: Adds special object handling for Suppliers
 - Added java support classes with static methods to match our kotlin extension methods (see https://github.com/episode6/mockspresso/pull/32)
 - **DEPRECATED** `QuickMockspresso` and entire `:mockspresso-quick` module 

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
