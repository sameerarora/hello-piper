pipeline{
    agent {
       label "docker"
    }
    stages {
        stage('Checkout code') {
            steps {
                checkout scm
            }
        }
        stage('build'){
            steps{
            sh """
                echo "building ${env.GIT_COMMIT}"
                ./mvnw clean install -DskipTests
               """
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
                sh """
                ./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=hello-piper:${env.GIT_COMMIT} -X
                """
            }
        }
        stage('Push image to docker registry'){
            steps{
                sh """
                    docker tag hello-piper:${env.GIT_COMMIT} localhost:5000/hello-piper:${env.GIT_COMMIT}
                    docker push localhost:5000/hello-piper:${env.GIT_COMMIT}
                """
            }
        }
    }
}