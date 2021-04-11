import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

class MavenConfiguration {

  static void setup(Project project) {
    MavenConfiguration config = new MavenConfiguration(project)
    config.setupDescriptions()
  }

  final Project project

  private MavenConfiguration(Project project1) {
    project = project1
  }

  private void setupDescriptions() {
    project.publishing {
      publications {
        mavenArtifacts(MavenPublication) {
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
        }
      }
    }
  }
}
