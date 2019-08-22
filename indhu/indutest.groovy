node {
/*
    properties([ 
parameters([
[$class: 'ChoiceParameter', 
choiceType: 'PT_SINGLE_SELECT', 
description: 'Select a choice', 
filterable: true, 
name: 'ENV', 
randomName: 'choice-parameter-7601235200970', 
script: [$class: 'GroovyScript', 
fallbackScript: [classpath: [], 
                sandbox: true, 
                script: 'return ["ERROR"]'], 
                script: [classpath: [], 
                         sandbox: true,
                        script: "return${devEnvList}"]
                ]
        ], 

[$class: 'ChoiceParameter', 
choiceType: 'PT_SINGLE_SELECT', 
description: 'Select a choice', 
filterable: true, 
name: 'SERVICE', 
randomName: 'choice-parameter-7601235200970', 
script: [$class: 'GroovyScript', 
fallbackScript: [classpath: [], 
                sandbox: true, 
                script: 'return ["ERROR"]'], 
                script: [classpath: [], 
                         sandbox: true,
                        script: "return${env.repo_list}"]
                ]
        ],

[$class: 'CascadeChoiceParameter', 
choiceType: 'PT_SINGLE_SELECT', 
description: 'Active Choices Reactive parameter',
filterable: true, 
name: 'Server', 
randomName: 'choice-parameter-7601237141171', 
referencedParameters: 'ENV', 
script: [$class: 'GroovyScript', 
fallbackScript: [classpath: [], 
                 sandbox: true,
                 script: 'return ["error"]'],
                script: [classpath: [], 
                sandbox: true, 
                script: 'if(ENV.equals("Dev")) {return [\'DEV1\', \'DEV2\', \'Dev3\', \'Dev4\']} else if(ENV.equals("Stag")) {return [\'Stag1\',\'Stag2\',\'Stag3\',\'Stag4\']} else {return [\'Prod1\',\'Prod2\']}']]
        ],

choice(choices: ['dev1', 'dev2', 'dev3', 'dev4'], description: '', name: 'Env'),
string(defaultValue: '', description: '', name: 'Artifact_Name', trim: true)
])
])
*/
properties([ parameters([[$class: 'ChoiceParameter', choiceType: 'PT_SINGLE_SELECT', description: 'Please select Service', filterLength: 1, filterable: true, name: 'Service', randomName: 'choice-parameter-532642778576920', script: [$class: 'GroovyScript', fallbackScript: [classpath: [], sandbox: false, script: 'return [ERROR]'], script: [classpath: [], sandbox: false, script: "return ${env.repo_list}" ]]], string(defaultValue: '', description: 'Please select a branch', name: 'Branch', trim: false), choice(choices: ['dev1', 'dev2', 'dev3', 'dev4', 'dev5', 'dev6'], description: 'please select env', name: 'Environment')])])


try {
        stage ('SCM') {
                script {
                  git branch: 'maven-tomcat', url: 'https://gitlab.com/beer786/newproject.git';
                }
                  }


        stage('Build') {
                //sh 'mvn clean package -DskipTests=true'     
                sh 'echo hei'
                }


stage('test') {
            //sh 'mvn test'
            sh 'echo helo'
               }
             
 
        stage('Archive') {

             // archiveArtifacts 'target/*zip'
             sh 'echo hleo'
                } 
             
        stage('Environment') {
              sh 'echo ecn'
               //sh '''
                //export instance = `cat env.json | jq -r .$Server.instance`
                //export path = `cat env.json | jq -r .$Server.instancePath`
                //'''
            }

} finally {

stage('junit')  {
             junit 'target/surefire-reports/*.xml'
        }

}

}
