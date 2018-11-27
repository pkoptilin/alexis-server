pipeline {
    agent any
    
    tools {nodejs "node8"}
    
    stages {
        
        stage('Lint'){
            steps{
                sh 'npm run lint'
                sh 'npm run lint:fix'
            }
        }
        stage('Test'){
            steps{
                sh 'npm run test'
            }
        }

        stage('Build'){
            steps{
                sh 'npm run build'
            }
        }
    }
}