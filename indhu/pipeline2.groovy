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
                        script: "return['Dev','Stag','Prod','Test']"]
                ]
        ], 
  
[$class: 'ChoiceParameter', 
choiceType: 'PT_SINGLE_SELECT', 
description: 'Select a choice', 
filterable: true, 
name: 'SERVICE', 
randomName: 'choice-parameter-7601235200972', 
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

string(defaultValue: '', description: '', name: 'Branch', trim: true)
])
])

pipeline {
    agent any
    environment {
    ARTIFACT_NAME="${env.JOB_NAME}-${env.BUILD_NUMBER}"
    D_ARTIFACT_NAME="gitlab-test-16-beermd"
    //instance = sh (returnStdout: true, script: "`cat env.json |jq -r '.$Server.instance'`")
    }
    tools { 
        maven 'maven-3.6.1' 
        jdk 'jdk8' 
        }
    stages {
        stage ('SCM') {
            steps {
                script {
                  git branch: 'maven-tomcat', url: 'https://gitlab.com/beer786/newproject.git';
                  dir('Configuration') {
                     git branch: 'master', url: 'https://gitlab.com/beer786/dummyproject.git'
                 }
                }
                  }
                }

        stage('Build') {
           steps {    
                sh "mvn clean package -DskipTests=true -DARTIFACT_NAME=$ARTIFACT_NAME"    
               // sh 'echo hei'
                }      
              }
        
       stage('ReadAnotheRepo') {
            steps {
                sh "cat Configuration/environments.json"
       }
       }
       stage('JsonAnotherRepo') {
            steps {
                script {
                jsonfile = readJSON file: 'Configuration/environments.json'
                echo "environment name is ${jsonfile.DEV1.instancePath}"
                }
            }
       }
        stage('test') {
         steps {
            sh 'mvn test'
            //sh 'echo helo'
               }
             }
 
        stage('Archive') {
            steps {
             archiveArtifacts 'target/*zip'
             //sh 'echo hleo'
                } 
             }  
        stage('publish_nexus') {
            steps {
                script {
                    pom = readMavenPom  file: "pom.xml"
                    pom2 = readMavenPom file:  "src/assembly/testpom.xml"
                    ArtId = pom.artifactId
                    GrpId = pom.groupId
                    Pkg = pom.packaging
                    zipid = pom2.groupId
                    echo "pom2 file ${zipid}"
                    nexusArtifactUploader artifacts: [[artifactId: ArtId, classifier: '', file: "target/maven-simple-${ARTIFACT_NAME}-SNAPSHOT.jar", type: Pkg ]], 
                    credentialsId: 'nexus-creds', 
                    groupId: GrpId, 
                    nexusUrl: 'localhost:8081', 
                    nexusVersion: 'nexus3',
                    protocol: 'http', 
                    repository: 'test-maven', 
                    version: '0.2.2'           
            }
            }
        }
        stage('Downstream') {
            steps {
                build job: 'tbd', 
                //parameters: [string(name: 'ARTIFACT_NAME', value: "${D_ARTIFACT_NAME}"),string(name: 'ENV', value: 'Dev'), string(name: 'Server', value: '18.207.244.125')]
                  parameters: [string(name: 'REPOS', value: 'repo2')]
                }
            }
    }
    
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
