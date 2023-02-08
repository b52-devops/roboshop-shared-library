def lintChecks(COMPONENT) {
        sh "echo Installing mvn"
        // sh "yum install maven -y"
        // sh "mvn checkstyle:check"
        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
}

def call(COMPONENT)                                                           // call is the default function that's called by default.
{
    pipeline{
        agent any
        environment {
            SONAR = credentials('SONAR')
            SONAR_URL = "172.31.15.252"
        }
        stages{                                                     // Start of the stages
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks(COMPONENT)                                // If the function is in the same file, no need to call the function with the fileName as prefix.
                    }
                }
            }

            stage('Sonar Checks') {
                steps {
                    script {
                        sh "mvn clean compile"
                        env.ARGS=" -Dsonar.java.binaries=target/"
                        common.sonarChecks(COMPONENT)                                // If the function is in the same file, no need to call the function with the fileName as prefix.
                    }
                }
            }

            stage('Test Cases'){
                    parallel{
                        stage('Unit Tests'){
                            steps{
                                sh "echo Unit Testing ..........."
                            }
                        }

                        stage('Integration Tests'){
                            steps{
                                sh "echo Integration Testing ..........."
                            }
                        }

                        stage('Functional Tests'){
                            steps{
                                sh "echo Functional Testing ..........."
                            }
                        }
                    }
                }

            stage('Preparing the artifact') {
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
                    }
                steps {
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                    sh "ls -ltr"
                }
            }

            stage('Uploading the artifact'){
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
                    }
                steps{
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                }
            }

        }                                                          // End of the stages
    }
}
