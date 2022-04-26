def teamName = 'refurbishment'
def appName = 'app'

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

  triggers {
    githubPullRequest {
      useGitHubHooks()
      permitAll()
      displayBuildErrorsOnDownstreamBuilds()
      extensions {
        commitStatus {
          context('jenkins')
          triggeredStatus('starting PR build of statistics-service')
          startedStatus('started PR build...')
          completedStatus('SUCCESS', 'Click on Details to see changes')
          completedStatus('FAILURE', 'Something went wrong')
          completedStatus('PENDING', 'Still in progress...')
          completedStatus('ERROR', 'Something went really wrong')
        }
      }
    }
  }
}

pipelineJob("refurbishment/carol/app-clean-infra") {
  displayName('App Clean Infra')
  description("Clean env, leave only existing branches")

  environmentVariables(
    TEAM_NAME: teamName,
    APP_NAME: appName,
    APP_REPO: "wkda/refurbishment-app",
    DOCKER_REGISTRY: "320294051391.dkr.ecr.eu-west-1.amazonaws.com",
    TF_STATES_S3_BUCKET: "refurbishment-terraform-app-states"
  )

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