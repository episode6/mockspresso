# ChangeLog

### v0.0.16-SNAPSHOT - unreleased

### v0.0.15-SNAPSHOT - unreleased
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
