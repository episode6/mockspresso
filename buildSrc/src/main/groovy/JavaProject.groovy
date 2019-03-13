import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

class JavaProject implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.with {
      plugins.with {
        apply 'java-library'
        apply 'me.tatarka.retrolambda'
        applyDeployablePlugin(it)
        apply 'com.episode6.hackit.gdmc'
      }

      sourceCompatibility = 1.8
      targetCompatibility = 1.8
      retrolambda {
        javaVersion JavaVersion.VERSION_1_7
      }
    }
  }

  protected void applyDeployablePlugin(PluginContainer plugins) {
    // no-op
  }
}