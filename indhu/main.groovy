def CreatePipelineJob(jobname, ptl) {
    pipelineJob("${jobname}") {
        logRotator {
            numToKeep(20)
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
