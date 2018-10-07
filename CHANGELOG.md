# ChangeLog

### v0.0.15-SNAPSHOT - unreleased
 - Convert api to kotlin for cleaner usage in kotlin code.
   - This includes a small change to the signature of `Mockspress.Builder#specialObjectMakers` that should only be breaking for legacy custom mockspresso extensions not using `mockspressod-extend`.

### v0.0.14 - June 28th, 2018
 - Add `Mockspresso.getDependency(DependencyKey)` to api
 - Add `@Dependency` annotation to api

### v0.0.13 - June 27th, 2018
 - introduce changelog
 - Remove `injector()` convenience method from main api
 - introduce `mockspresso-extend` library
 - refactor `mockspresso-quick` to build upon `mockspresso-extend`
 - enter the modern world of 2016 and apply retrolambda to the project
