import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

class MavenConfiguration {

  static void setup(Project project) {
    configureTasks(project)
    configurePublications(project)
  }

  private static boolean isReleaseBuild(Project project) {
    return project.version.contains("SNAPSHOT") == false
  }

  private static void configurePublications(Project project) {
    project.publishing {
      publications {
        mavenJava(MavenPublication) {
          from project.components.java

          groupId project.group
          artifactId project.name
          version project.version

          pom {
            name = project.name
            description = project.findProperty("deployable.pom.description")
            url = project.findProperty("deployable.pom.url")
            packaging "jar"
            licenses {
              license {
                name = project.findProperty("deployable.pom.license.name")
                url = project.findProperty("deployable.pom.license.url")
                distribution = project.findProperty("deployable.pom.license.distribution")
              }
            }
            developers {
              developer {
                id = project.findProperty("deployable.pom.developer.id")
                name = project.findProperty("deployable.pom.developer.name")
                if (project.findProperty("deployable.pom.developer.email") != null) {
                  email = project.findProperty("deployable.pom.developer.email")
                }
              }
            }
            scm {
              url = project.findProperty("deployable.pom.scm.url")
              connection = project.findProperty("deployable.pom.scm.connection")
              developerConnection = project.findProperty("deployable.pom.scm.developerConnection")
            }
          }

          artifact project.sourcesJar
          artifact project.javadocJar
        }
      }
    }
  }

  private static void configureTasks(Project project) {
    project.with {
      task("javadocJar", type: Jar, dependsOn: tasks.dokkaHtml) {
        archiveClassifier.set('javadoc')
        from tasks.dokkaHtml
      }

      task("sourcesJar", type: Jar) {
        from sourceSets.main.allSource
        archiveClassifier.set('sources')
      }

      task("deploy", dependsOn: tasks.publish) {
        description = "A simple alias for publish, because it's more fun to say."
        group = "publishing"
      }

      task('install', dependsOn: tasks.publishToMavenLocal) {
        description = 'A simple alias for publishToMavenLocal to maintain compatibility with old versions of deployable.'
        group = 'publishing'
      }
    }
  }

}

