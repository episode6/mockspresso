import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Sync

class DeployableJavaProject extends JavaProject {

  @Override
  void apply(Project project) {
    super.apply(project)

    project.task(Tasks.SYNC_DOCS, type: Sync) {
      from project.tasks.dokka.outputDirectory
      into "${project.rootProject.rootDir}/docs/javadocs/${project.name}"
      dependsOn project.tasks.dokka

      // this file doesn't get generated correctly in the current version of dokka
      // each link in it has two nested <a href> tags, where the inner-one is correct
      // but the outer one references the absolute path where the docs were first
      // generated (i.e. my laptop).
      // This file also isn't linked anywhere from the rest of the javadocs
      // so excluding it for now should be NBD.
      excludes += "${project.name}/index-outline.html"
    }
  }

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.apply 'com.episode6.hackit.deployable.kt.jar'
  }
}
