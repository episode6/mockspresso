import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ExternalDependency

import javax.annotation.Nullable

/**
 * Configurations/urls for external documentation
 */
class ExternalDocs {

  static String JAVAX_INJECT_DOCS_URL = "https://docs.oracle.com/javaee/7/api/"

  static @Nullable String getUrlFor(ExternalDependency dependency) {
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

  static void addLink(Project project, String externalUrl) {
    project.tasks.dokkaHtmlPartial {
      dokkaSourceSets { configureEach { externalDocumentationLink { url.set(new URL(externalUrl)) } } }
    }
  }

  static void configureSourcesFor(Project project) {
    project.configurations.all { Configuration config ->
      config.withDependencies { dependencies ->
        dependencies.withType(ExternalDependency)
            .collect { getUrlFor(it) }
            .findAll { it != null }
            .forEach { extUrl -> addLink(project, extUrl) }
      }
    }
  }
}
