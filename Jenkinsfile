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
                echo "Starting build....."
                ./mvnw clean install
                echo "Done!!"
               '''
            }
        }
    }
}