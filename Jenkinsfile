pipeline {
    agent any
    tools {
        
        //  Install maven tool.
        maven "Maven_3.9.16"
    }
    stages {
        stage ('Tools check') {
            steps {
                sh "echo Print maven version"
                sh "mvn -version"
            }
        }
        stage ('Build') {
            steps {
                
                // Get code from a GitHub repo.
                git 'https://github.com/Dany0596/devTrack.git'

                //  It runs the scripts from the backend directory.
                dir ('backend') {
                
                    // Clean package and skips tests.
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }
        stage ('Unit tests') {
            steps {    
                
                //  It runs the scripts from the backend directory.
                dir ('backend') {

                    //  Runs tests
                    sh "mvn test"
                }
            }
            post {
                always {
                    junit "backend/target/surefire-reports/*.xml"
                }
            }
        }
    }
}
