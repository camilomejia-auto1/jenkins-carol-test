pipeline {
  agent {
    label 'docker-builder'
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '30'))
    timeout(time: 30, unit: 'MINUTES')
    timestamps()
    parallelsAlwaysFailFast()
    disableConcurrentBuilds()
  }

  stages {
    stage('Startup') {
      steps {
        sh("echo Startup")
      }
    }
  }
}
