import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaProject implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.plugins.apply('java-library')
    project.plugins.apply('me.tatarka.retrolambda')
    applyDeployablePlugin(project)
    project.plugins.apply('com.episode6.hackit.gdmc')
    project.sourceCompatibility = 1.8
    project.targetCompatibility = 1.8
    project.retrolambda {
      javaVersion JavaVersion.VERSION_1_7
    }
  }

  protected void applyDeployablePlugin(Project project) {
    // no-op
  }
}