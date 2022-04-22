folder('refurbishment') {
  displayName('Refurbishment')
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
                  remote("git@github.com:camilomejia-auto1/jenkins-carol-test")
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
