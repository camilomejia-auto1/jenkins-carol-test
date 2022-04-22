folder('refurbishment') {
  displayName('Refurbishment 3')
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
                  remote("camilomejia-auto1/jenkins-carol-test")
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

pipelineJob('refurbishment/carol') {
  displayName('Carol app')

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