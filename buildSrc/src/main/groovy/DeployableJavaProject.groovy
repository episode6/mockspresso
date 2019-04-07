import org.gradle.api.plugins.PluginContainer

class DeployableJavaProject extends JavaProject {

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.apply 'com.episode6.hackit.deployable.kt.jar'
  }
}
