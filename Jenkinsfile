pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Build'
                bat './gradlew build'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
        }
    }
}
