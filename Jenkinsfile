@Library('refurbishment') _

pipeline {
  agent {
    label 'built-in'
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
        envInit()
      }
    }
  }
}
