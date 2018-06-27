import org.gradle.api.Project

class DeployableJavaProject extends JavaProject{

  @Override
  protected void applyDeployablePlugin(Project project) {
    project.plugins.apply('com.episode6.hackit.deployable.jar')
  }
}
