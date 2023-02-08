def lintChecks(COMPONENT) {
        sh "echo Installing mvn"
        // sh "yum install maven -y"
        // sh "mvn checkstyle:check"
        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
}

def sonarChecks(COMPONENT) {
        sh "echo Starting code quality analysis"
        sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 -Dsonar.java.binaries=target/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
        sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
        sh "bash -x quality-gata.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${COMPONENT}"
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
                        sonarChecks(COMPONENT)                                // If the function is in the same file, no need to call the function with the fileName as prefix.
                    }
                }
            }

            stage('Downloading the dependencies') {
                steps {
                    sh "mvn clean package"
                }
            }
        }                                                          // End of the stages
    }
}
