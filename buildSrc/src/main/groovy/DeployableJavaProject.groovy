import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.bundling.Jar

class DeployableJavaProject extends JavaProject {

  @Override
  protected void applyDeployablePlugin(PluginContainer plugins) {
    plugins.apply 'org.jetbrains.dokka'

  }

  @Override
  void apply(Project project) {
    super.apply(project)

    project.with {
      configurations {
        mavenOptional
        mavenProvided

        compileOnly {
          extendsFrom(
              mavenOptional,
              mavenProvided)
        }
      }

      task("javadocJar", type: Jar, dependsOn: tasks.dokka) {
        archiveClassifier = 'javadoc'
        from tasks.dokka
      }

      task("sourcesJar", type: Jar) {
        from sourceSets.main.allSource
        archiveClassifier = 'sources'
      }

      task(Tasks.SYNC_DOCS, type: Sync) {
        from tasks.dokka.outputDirectory
        into "${rootProject.rootDir}/docs/javadocs/${name}"
        dependsOn tasks.dokka

        // this file doesn't get generated correctly in the current version of dokka
        // each link in it has two nested <a href> tags, where the inner-one is correct
        // but the outer one references the absolute path where the docs were first
        // generated (i.e. my laptop).
        // This file also isn't linked anywhere from the rest of the javadocs
        // so excluding it for now should be NBD.
        excludes += "${name}/index-outline.html"
      }

      tasks.dokka {
        jdkVersion = 8
      }

      configurations.all { Configuration config ->
        //noinspection UnstableApiUsage
        config.withDependencies { dependencies ->

          // Each module's javadocs are generated separately and can't see each other (atm).
          // This block links to our already hosted docs from the previous release.
          // It's not ideal, but its better than nothing.
          dependencies.findAll { it instanceof ProjectDependency }
              .forEach { dep ->
                project.tasks.dokka {
                  externalDocumentationLink {
                    url = new URL("https://episode6.github.io/mockspresso/javadocs/${dep.name}/${dep.name}/")
                  }
                }
              }

//          // Include links to javadocs of external dependencies when possible
//          dependencies.findAll { it instanceof ExternalDependency }
//              .collect { (ExternalDependency) it }
//              .collect { ExternalDocs.getUrlFor(project, it) }
//              .findAll { it != null }
//              .forEach { extUrl ->
//                tasks.dokka { externalDocumentationLink { url = extUrl } }
//              }
        }
      }
    }
  }
}
