pipelineJob("refurbishment/clean-fe") {
  description("Clean env, leave only existing branches")

  definition {
    cps {
      script(readFileFromWorkspace('jobs/Jenkinsfile'))
      sandbox()
    }
  }

  triggers {
    cron('@daily')
  }
}