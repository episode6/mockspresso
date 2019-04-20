import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalDependency

import javax.annotation.Nullable

/**
 *
 */
class ExternalDocs {
  private static final String VERSION_TAG = "<version>"
  private static final Map<String, String> DOCS_MAP = [
      'junit:junit'                                 : "https://junit.org/junit4/javadoc/$VERSION_TAG/",
      'com.google.dagger:dagger'                    : "https://google.github.io/dagger/api/$VERSION_TAG/",
      'javax.inject:javax.inject'                   : "https://docs.oracle.com/javaee/7/api/",
      'org.easymock:easymock'                       : "http://easymock.org/api/",
      'com.google.guava:guava'                      : "https://google.github.io/guava/releases/$VERSION_TAG/api/docs/",
      'org.mockito:mockito-core'                    : "https://static.javadoc.io/org.mockito/mockito-core/$VERSION_TAG/",
      'org.powermock:powermock-module-junit4'       : "https://static.javadoc.io/org.powermock/powermock-module-junit4/$VERSION_TAG/",
      'org.powermock:powermock-api-easymock'        : "https://static.javadoc.io/org.powermock/powermock-api-easymock/$VERSION_TAG/",
      'org.powermock:powermock-module-junit4-rule'  : "https://static.javadoc.io/org.powermock/powermock-module-junit4-rule/$VERSION_TAG/",
      'org.powermock:powermock-classloading-xstream': "https://static.javadoc.io/org.powermock/powermock-classloading-xstream/$VERSION_TAG/",
      'org.powermock:powermock-api-mockito2'        : "https://static.javadoc.io/org.powermock/powermock-api-mockito2/$VERSION_TAG/",
  ]

  static @Nullable URL getUrlFor(Project project, ExternalDependency dependency) {
    String key = "${dependency.group}:${dependency.name}"
    if (!DOCS_MAP.containsKey(key)) {
      return null
    }

    String url = DOCS_MAP.get(key)
    @Nullable String version = lookupVersion(project, key)
    if (version != null) {
      url = url.replace(VERSION_TAG, version)
    }
    return new URL(url)
  }

  private static @Nullable String lookupVersion(Project project, String key) {
    try {
      return project.gdmcVersion(key)
    } catch (GradleException ignore) {
      return null
    }
  }
}
