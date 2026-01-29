pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Pulling from GITHUB repository"
                git branch: 'main',
                    credentialsId: 'mygithubcred',
                    url: 'https://github.com/varshagk2709/varshagk2709-jenkins.git'
            }
        }

        stage('Test the Project') {
            steps {
                echo "Test my JAVA project"
                bat 'mvn clean test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                    echo 'Test Run completed!'
                }
            }
        }

        stage('Build Project') {
            steps {
                echo "Building my JAVA project"
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build the Docker Image') {
            steps {
                echo "Build the Docker Image for mvn project"
                bat 'docker build -t mvnproj:1.0 .'
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                echo "Push Docker Image to DockerHub for mvn project"
                withCredentials([usernamePassword(
                    credentialsId: 'mydockercred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat """
                    echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                    docker tag mvnproj:1.0 %DOCKER_USER%/mymvnproj:latest
                    docker push %DOCKER_USER%/mymvnproj:latest
                    """
                }
            }
        }

        stage('Deploy the project using Container') {
            steps {
                echo "Running Java Application in Docker Container"
                withCredentials([usernamePassword(
                    credentialsId: 'mydockercred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat """
                    docker rm -f myjavaappcont || echo Container not found
                    docker run -d --name myjavaappcont %DOCKER_USER%/mymvnproj:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'I succeeded!'
        }
        failure {
            echo 'Failed........'
        }
    }
}