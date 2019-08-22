def CreatePipelineJob(jobname, ptl) {
    pipelineJob("${jobname}") {
        logRotator {
            numToKeep(20)
        }
    environmentVariables {
            envs('repo_list': "${repo_names}")
            envs('env_data': "${env_data}")
            envs('env_list': "${env_list}")
            envs('devEnvList': "${devEnvList}")
        }

        //displayName("#${BUILD_NUMBER} ${ENV}")
        definition {
            cps {
                script(readFileFromWorkspace("indhu/${ptl}.groovy"))
                sandbox()
                }
                }
                }

}

CreatePipelineJob("Indu-CI-test", "indutest")
