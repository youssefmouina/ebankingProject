pipeline {
    agent any

    stages {
        stage('Client Frontend Build') {
            when {
                changeset "projet-ebanking-EbankingDevFront/**"
            }
            steps {
                script {
                    echo "Ebanking client frontend Build"
                }
            }
        }

        stage('Backend Build Main') {
            when {
                changeset "projet-ebanking-ebankingSecuredDev/**"
            }
            steps {
                script {
                    echo "Ebanking backend Build"
                }
            }
        }
    }
}
