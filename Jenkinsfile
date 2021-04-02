pipeline{
    agent any
    stages {
        stage('Checkout code') {
            steps {
                checkout scm
            }
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
                GIT_COMMIT_REV = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
                sh '''
                ./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=hello-piper:${GIT_COMMIT_REV}
                '''
            }
        }
    }
}