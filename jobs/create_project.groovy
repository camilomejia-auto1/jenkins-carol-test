folder('refurbishment') {
  displayName('Refurbishment')
  description("Folder for refurbishment's jobs")
}

import com.auto1.retail.Team

def team = new Team(
  dslFactory: this,
  jenkinsEnv: 'qa',
  teamName: 'refurbishment',
  dockerRegistryId: '177118070538',
  tfStatesS3Bucket: 'retail-terraform-app-states'
)

team.folder {}

team.pullRequestJob('app')
team.autobuildBranchJob('app', 'develop')