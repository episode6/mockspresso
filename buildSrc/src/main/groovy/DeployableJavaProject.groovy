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

      tasks.dokkaHtml {
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
                tasks.dokkaHtml {
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
