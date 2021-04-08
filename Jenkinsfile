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
                 docker build -t hello-piper:${env.GIT_COMMIT} .
                """
            }
        }

        stage('Deploy application to EKS'){
            steps{
                    sh """
                       helm install hello-piper ./hello-piper
                    """
            }
        }
    }
}