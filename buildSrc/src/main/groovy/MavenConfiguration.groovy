import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

class MavenConfiguration {

  static void setup(Project project) {
    configureTasks(project)
    configurePublications(project)
    configureRepo(project)
    configureSigning(project)
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
            description = "An extensible auto-mocker for java, designed to simplify your unit tests."
            url = "https://github.com/episode6/mockspresso"
            packaging "jar"

            licenses {
              license {
                name = "The MIT License (MIT)"
                url = "https://github.com/episode6/mockspresso/blob/master/LICENSE"
                distribution = "repo"
              }
            }
            developers {
              developer {
                id = "episode6"
                name = "episode6, Inc."
              }
            }
            scm {
              url = "extensible"
              connection = "scm:https://github.com/episode6/mockspresso.git"
              developerConnection = "scm:https://github.com/episode6/mockspresso.git"
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

  private static boolean isReleaseBuild(Project project) {
    return project.version.contains("SNAPSHOT") == false
  }

  private static String getRepoUrl(Project project) {
    if (isReleaseBuild(project)) {
      return "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    } else {
      return "https://oss.sonatype.org/content/repositories/snapshots/"
    }
  }

  private static void configureRepo(Project project) {
    project.publishing {
      repositories {
        maven {
          url getRepoUrl(project)
          credentials {
            username project.findProperty("deployable.nexus.username")
            password project.findProperty("deployable.nexus.password")
          }
        }
      }
    }
  }

  private static void configureSigning(Project project) {
    if (isReleaseBuild(project)) {
      project.signing {
        sign project.publishing.publications.mavenJava
      }
    }
  }
}

