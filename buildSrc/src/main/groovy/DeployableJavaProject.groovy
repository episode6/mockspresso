import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ExternalDependency
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.bundling.Jar

class DeployableJavaProject extends JavaProject {

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.apply 'org.jetbrains.dokka'

  }

  @Override
  void apply(Project project) {
    super.apply(project)

    project.with {
      configurations {
        mavenOptional
        mavenProvided

        compileOnly {
          extendsFrom(mavenOptional, mavenProvided)
        }
      }

      task("javadocJar", type: Jar, dependsOn: tasks.dokkaHtml) {
        archiveClassifier.set('javadoc')
        from tasks.dokkaHtml
      }

      task("sourcesJar", type: Jar) {
        from sourceSets.main.allSource
        archiveClassifier.set('sources')
      }

      // dokkaHtml is used to generate javadoc jars for maven uploads
      tasks.dokkaHtml {
        dokkaSourceSets {
          configureEach {
            jdkVersion.set(8)
            reportUndocumented.set(false)
            noStdlibLink.set(true)
            noJdkLink.set(true)
          }
        }
      }

      // dokkaHtmlPartial is used to generate a complete set of docs for the web
      tasks.dokkaHtmlPartial {
        dokkaSourceSets {
          configureEach {
            jdkVersion.set(8)
            reportUndocumented.set(false)
            noStdlibLink.set(false)
            noJdkLink.set(false)
          }
        }
      }

      configurations.all { Configuration config ->
        config.withDependencies { dependencies ->
          // Include links to javadocs of external dependencies when possible
          dependencies.findAll { it instanceof ExternalDependency }
              .collect { (ExternalDependency) it }
              .collect { ExternalDocs.getUrlFor(it) }
              .findAll { it != null }
              .forEach { extUrl ->
                tasks.dokkaHtmlPartial {
                  dokkaSourceSets {
                    configureEach { externalDocumentationLink { url.set(extUrl) } }
                  }
                }
              }
        }
      }
    }
  }
}
