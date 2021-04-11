import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.bundling.Jar

class DeployableJavaProject extends JavaProject {

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.with {
      apply 'org.jetbrains.dokka'
      apply 'maven-publish'
      apply 'signing'
    }
  }

  @Override
  void apply(Project project) {
    super.apply(project)

    project.with {
      task("javadocJar", type: Jar, dependsOn: tasks.dokkaHtml) {
        archiveClassifier.set('javadoc')
        from tasks.dokkaHtml
      }

      task("sourcesJar", type: Jar) {
        from sourceSets.main.allSource
        archiveClassifier.set('sources')
      }

      task("deploy", dependsOn: tasks.publish) {
        description = "A simple alias for publish, because it's more fun to say."
        group = "publishing"
      }

      task('install', dependsOn: tasks.publishToMavenLocal) {
        description = 'A simple alias for publishToMavenLocal to maintain compatibility with old versions of deployable.'
        group = 'publishing'
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
    }

    ExternalDocs.configureSourcesFor(project)
    MavenConfiguration.setup(project)
  }
}
