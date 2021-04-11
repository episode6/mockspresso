import org.gradle.api.artifacts.ExternalDependency

import javax.annotation.Nullable

/**
 * Urls for external documentation
 */
class ExternalDocs {
  
  static String JAVAX_INJECT_DOCS_URL = "https://docs.oracle.com/javaee/7/api/"

  static @Nullable String getUrlStringFor(ExternalDependency dependency) {
    String key = "${dependency.group}:${dependency.name}"
    String version = dependency.getVersion()
    switch (key) {
      case 'junit:junit':
        return "https://junit.org/junit4/javadoc/$version/"
      case 'javax.inject:javax.inject':
        return JAVAX_INJECT_DOCS_URL
      case 'org.easymock:easymock':
        return "http://easymock.org/api/"
      case 'com.google.guava:guava':
        return "https://google.github.io/guava/releases/$version/api/docs/"
      case 'org.mockito:mockito-core':
        return "https://static.javadoc.io/org.mockito/mockito-core/$version/"
      case 'org.powermock:powermock-module-junit4':
        return "https://static.javadoc.io/org.powermock/powermock-module-junit4/$version/"
      case 'org.powermock:powermock-api-easymock':
        return "https://static.javadoc.io/org.powermock/powermock-api-easymock/$version/"
      case 'org.powermock:powermock-module-junit4-rule':
        return "https://static.javadoc.io/org.powermock/powermock-module-junit4-rule/$version/"
      case 'org.powermock:powermock-classloading-xstream':
        return "https://static.javadoc.io/org.powermock/powermock-classloading-xstream/$version/"
      case 'org.powermock:powermock-api-mockito2':
        return "https://static.javadoc.io/org.powermock/powermock-api-mockito2/$version/"
    }
    return null;
  }

  static @Nullable URL getUrlFor(ExternalDependency dependency) {
    @Nullable String url = getUrlStringFor(dependency)
    if (url != null) {
      return new URL(url)
    }
    return null
  }
}
