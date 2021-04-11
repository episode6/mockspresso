import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

class JavaProject implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.with {
      plugins.with {
        apply 'java-library'
        apply 'kotlin'
        applyDeployablePlugin(it)
      }

      sourceCompatibility = 1.8
      targetCompatibility = 1.8

      dependencies {
        implementation libs.kotlin.stdlib
      }
    }
  }

  protected void applyDeployablePlugin(PluginContainer plugins) {
    // no-op
  }
}
