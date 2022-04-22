folder('refurbishment') {
  displayName('Refurbishment')
  description("Folder for refurbishment's jobs")
}

folder('refurbishment/carol') {
  displayName('Carol')
  description("Folder for refurbishment's jobs")

  properties {
    folderLibraries {
      libraries {
        libraryConfiguration {
          name('refurbishment')
          implicit(false)
          retriever {
            scmSourceRetriever {
              scm {
                gitSCMSource {
                  remote("https://github.com/camilomejia-auto1/jenkins-carol-test.git")
                }
              }
            }
          }
          defaultVersion('master')
        }
      }
    }
  }
}

pipelineJob('refurbishment/carol/app-deployment') {
  displayName('QA App Deployment')

  definition {
    cpsScm {
      scm {
        git {
          branch('origin/master')
          remote {
            github('camilomejia-auto1/jenkins-carol-test')
          }
          extensions {
            cloneOptions {
              depth(1)
              shallow(true)
            }
          }
        }
      }
    }
  }
}

pipelineJob("refurbishment/carol/app-clean-infra") {
  displayName('App Clean Infra')
  description("Clean env, leave only existing branches")

  definition {
    cps {
      script(readFileFromWorkspace('jobs/carol/app-clean-infra/Jenkinsfile'))
      sandbox()
    }
  }

  triggers {
    cron('@daily')
  }
}