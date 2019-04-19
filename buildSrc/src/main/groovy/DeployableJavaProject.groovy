import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
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

    project.tasks.dokka {
      jdkVersion = 8
    }

    // Each module's javadocs are generated separately and can't see each other (atm).
    // This block links to our already hosted docs from the previous release.
    // It's not ideal, but its better than nothing.
    project.configurations.all { Configuration config ->
      //noinspection UnstableApiUsage
      config.withDependencies { dependencies ->
        dependencies.findAll { it instanceof ProjectDependency }
            .forEach { dep ->
              project.tasks.dokka {
                externalDocumentationLink {
                  url = new URL("https://episode6.github.io/mockspresso/javadocs/${dep.name}/${dep.name}/")
                }
              }
            }
      }
    }
  }

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.apply 'com.episode6.hackit.deployable.kt.jar'
  }
}
