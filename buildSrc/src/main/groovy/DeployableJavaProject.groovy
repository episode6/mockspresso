import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

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
