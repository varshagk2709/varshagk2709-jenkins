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
                echo "Running Maven Tests"
                bat 'mvn clean test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                    echo 'Test stage completed'
                }
            }
        }

        stage('Build Project') {
            steps {
                echo "Building Maven Project"
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker Image"
                bat 'docker build -t myapp:1.0 .'
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'mydockercred',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat '''
                    docker logout
                    echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                    docker tag myapp:1.0 %DOCKER_USER%/myapp:latest
                    docker push %DOCKER_USER%/myapp:latest
                    '''
                }
            }
        }

        stage('Deploy the project using k8s') {
            steps {
                echo "Deploying to Kubernetes (Minikube)"
                bat '''
                "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" delete
                "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" start
                "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" status

                kubectl apply -f deployment.yaml
                timeout /t 20
                kubectl get pods

                kubectl apply -f services.yaml
                timeout /t 10
                kubectl get services
                '''
            }
        }

        stage('Parallel Loading of Services and Dashboard') {
            parallel {

                stage('Run Minikube Dashboard') {
                    steps {
                        bat '''
                        "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" addons enable metrics-server
                        "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" dashboard
                        '''
                    }
                }

                stage('Run Minikube Services') {
                    steps {
                        bat '''
                        "C:\\Program Files\\Kubernetes\\Minikube\\minikube.exe" service --all
                        '''
                    }
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
