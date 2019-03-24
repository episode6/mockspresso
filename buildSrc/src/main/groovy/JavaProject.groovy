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
        apply 'com.episode6.hackit.gdmc'
      }

      sourceCompatibility = 1.8
      targetCompatibility = 1.8

      dependencies {
        implementation 'org.jetbrains.kotlin:kotlin-stdlib'
      }
    }
  }

  protected void applyDeployablePlugin(PluginContainer plugins) {
    // no-op
  }
}
