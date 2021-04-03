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
            env.GIT_COMMIT_ID = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
            sh '''
                echo "Running build for head at ${env.GIT_COMMIT_ID}"
               '''
        }
        stage('build'){
            steps{
            sh '''
                echo "building ${env.GIT_COMMIT}"
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