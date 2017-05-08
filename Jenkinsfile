#! groovy

node {
  stage('checkout') {
    checkout scm
    sh 'git submodule update --init'
  }

  def gradleRunner
  stage('pipeline') {
    gradleRunner = fileLoader.fromGit(
        'gradle/GradleRunner',
        'git@github.com:episode6/jenkins-pipelines.git',
        'v0.0.5',
        null,
        '')
  }

  gradleRunner.buildAndTest()
  gradleRunner.maybeDeploy()
}
