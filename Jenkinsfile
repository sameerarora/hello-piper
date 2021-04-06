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
                ./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=hello-piper:${env.GIT_COMMIT}
                """
            }
        }
        stage('Push image to docker registry'){
            steps{
                sh """
                    docker tag hello-piper:${env.GIT_COMMIT} sameerarora11/hello-piper:v0.0.1
                    docker push sameerarora11/hello-piper:v0.0.1
                """
            }
        }
        stage('Deploy application to Kubernetes cluster'){
            steps{
                sh """
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
                """
            }
        }
    }
}