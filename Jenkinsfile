pipeline{
     agent {
        kubernetes {
          inheritFrom 'helm'
          defaultContainer 'jenkins-slave-ssr'
        }
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

        stage('Deploy application to EKS'){
            steps{
                    sh """
                       helm install name hello-piper ./hello-piper
                    """
            }
        }
    }
}