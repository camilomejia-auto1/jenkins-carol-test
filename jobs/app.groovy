folder('refurbishment') {
  displayName('Refurbishment')
  description("Folder for refurbishment's jobs")
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