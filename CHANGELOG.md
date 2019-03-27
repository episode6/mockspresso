# ChangeLog

### v0.0.17 - unreleased
 - Add kotlin support
 - Replace jsr305 with jetbrains annotations
 - Annotate interfaces null/not null for kotlin compatibility
 - Remove retrolambda and target Java 8
 - Hide internal entry-point using kotlin `internal` visibility that was formerly public but not intended for public use.

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
