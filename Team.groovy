package com.auto1.retail

import javaposse.jobdsl.dsl.DslFactory

class Team {
  DslFactory dslFactory
  String jenkinsEnv
  String teamName
  String dockerRegistryId
  String tfStatesS3Bucket
  List recipientList

  void folder(Closure closure) {
    dslFactory.folder(teamName, {
      description("Jenkins $jenkinsEnv jobs for $teamName team")
    } << closure)
  }

  def pipelineJob(
    String appName,
    String branchName = '$GIT_BRANCH',
    String jobName = 'deployment'
  ) {
    Map jenkinsEnvConfig = [
      qa: [
        envs: ['qa', 'staging'], // default ENV: qa (since it is the first in choices)
        branchType: 'BRANCH',
        defaultBranch: 'origin/develop',
        selectedBranch: 'DEFAULT',
        branchSortMode: 'ASCENDING_SMART',
        buildByDefault: true
      ],
      prod: [
        envs: ['staging', 'preprod', 'prod'], // default ENV: staging (since it is the first in choices)
        branchType: 'TAG',
        defaultBranch: 'origin/master',
        selectedBranch: 'TOP',
        branchSortMode: 'DESCENDING_SMART',
        buildByDefault: false
      ]
    ][jenkinsEnv]
    String appRepo = "wkda/$teamName-$appName"
    String dockerRegistry = "${dockerRegistryId}.dkr.ecr.eu-west-1.amazonaws.com"
    
    dslFactory.pipelineJob("$teamName/$appName-$jobName") {
      displayName("$jenkinsEnv $appName $jobName")
      parameters {
        choiceParam(
          'ENV',
          jenkinsEnvConfig.envs,
          'Environment to deploy to'
        )
        gitParam('GIT_BRANCH') {
          type(jenkinsEnvConfig.branchType)
          defaultValue(jenkinsEnvConfig.defaultBranch)
          tagFilter('v*')
          sortMode(jenkinsEnvConfig.branchSortMode)
        }
        booleanParam(
          'BUILD_DOCKER_IMAGE',
          jenkinsEnvConfig.buildByDefault,
          'Force build docker image'
        )
      }
      environmentVariables(
        TEAM_NAME: teamName,
        APP_NAME: appName,
        APP_REPO: appRepo,
        DOCKER_REGISTRY: dockerRegistry,
        TF_STATES_S3_BUCKET: tfStatesS3Bucket
      )
      definition {
        cpsScm {
          scm {
            git {
              branch(branchName)
              remote {
                github(appRepo, 'ssh')
                credentials('github-ssh-rw-key')
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
  }

  void pullRequestJob(String[] args) {
    pipelineJob(*args).with {
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
  }

  void autobuildBranchJob(appName, branchName) {
    String safeBranchName = branchName.replaceAll(/[^\w]/, '')
    pipelineJob(appName, branchName, "autobuild-$safeBranchName").with {
      triggers {
        githubPush()
      }
    }
  }

  void liveJob(appName) {
    pipelineJob(appName, '$GIT_BRANCH', 'live-deployment')
  }
}