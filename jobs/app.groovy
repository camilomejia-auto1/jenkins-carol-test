folder('refurbishment') {
  displayName('Refurbishment')
  description("Folder for refurbishment's jobs")
}

pipelineJob('refurbishment/carol') {
  displayName('Carol app')
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