def jobs_repo = 'camilomejia-auto1/jenkins-carol-test'
def track_branch = 'master'

freeStyleJob("seed") {
  displayName("seed_job")
  description("Create jobs from dsl files")
  logRotator(30, 90)
  label("master")

  scm {
    git {
      remote {
        github(jobs_repo)
        branck(trak_branch)
      }
    }
  }
}