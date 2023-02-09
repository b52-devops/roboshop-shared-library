def sonarChecks() {
        stage ('Sonar Checks')
        {
                sh "echo Starting code quality analysis"
                // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 ${ARGS} -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
                // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
                // sh "bash -x quality-gata.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${COMPONENT}"
                // Uncomment above 3 lines only if your Sonarcude is available. Keeping cost in mind, we marked them as comments. but , in lab we have practiced the sonar checks and quality gates.
                sh "echo Code quality checks completed"
        }
}

def lintChecks() {
        stage('Lint Checks') {
                if(env.APP == "maven") {
                        sh "echo Installing mvn"
                        // sh "yum install maven -y"
                        // sh "mvn checkstyle:check"
                        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
                }
                else if(env.APP == "nodejs"){
                        sh "echo Installing JSLINT"
                        sh "npm install jslint"
                        sh "ls -ltr node_modules/jslint/bin/"
                        // sh "./node_modules/jslint/bin/jslint.js server.js"
                        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
                }
                else if(env.APP == "python"){
                        sh "echo Installing PYLINT"
                        // https://pylint.pycqa.org/en/2.7.1/user_guide/run.html
                        // sh pylint filename.py
                        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
                }
                else if (env.APP == "angularjs") {
                        sh "echo Installing ANGUAR LINT"
                        sh "echo lint checks completed for ${COMPONENT} .....!!!!!"
                }
                else
                        sh "echo performing generic lint cheks"

        }
}

def testCases() {
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
}

def testCases () {
        parallel(
                "UNIT": {
                        stage('Unit Tests'){
                                sh "echo Unit Testing ..........."
                        }
                },
                "INTEGRATION": {
                        stage('Integration Tests'){
                                sh "echo Integration Testing ..........."
                        }
                },
                "FUNCTIONAL": {
                        stage('Functional Tests'){
                                sh "echo Functional Testing ..........."
                        }
                },
        )
}