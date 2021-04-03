pipeline{
    agent any
    environment{
        GIT_COMMIT_ID = ""
    }
    stages {
        stage('Checkout code') {
            steps {
                checkout scm
            }
        }
        stage('Configure'){
            steps{
                    script{
                        env.GIT_COMMIT_ID = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
                    }
            }
            
        }
        stage('build'){
            steps{
            sh '''
                echo "building ${env.GIT_COMMIT_ID}"
                ./mvnw clean install -DskipTests
               '''
            }
        }
        stage('test'){
            steps{
                sh '''
                ./mvnw test
               '''
            }
        }
        stage('build docker image'){
            steps{
                sh '''
                ./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=hello-piper:${env.GIT_COMMIT_ID}
                '''
            }
        }
    }
}