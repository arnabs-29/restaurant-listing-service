pipeline {
    agent any

    environment{
        DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
        VERSION = "${env.BUILD_ID ?: 'latest'}"
    }

    tools{
        maven "Maven"
    }

    stages{

        stage('Maven Build'){
            steps{
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Unit Tests'){
            steps{
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis'){
            steps{
                sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar -Dsonar.host.url=http://3.104.228.152:9000/ -Dsonar.login=squ_5e455016e5aa3750d31492c4df4111f7a4c55d75'
            }
        }

        stage('Check code coverage'){
            steps{
                script{
                    def token= "squ_5e455016e5aa3750d31492c4df4111f7a4c55d75"
                    def sonarQubeUrl= "http://3.104.228.152:9000/api"
                    def componentKey="com.foodapp:restaurant"
                    def coverageThreshold=80.0

                    def response = sh(
                        script: "curl -H 'Authorization: Bearer ${token}' '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'", 
                        returnStdout: true
                    ).trim()

                    def coverage = sh(
                        script: "echo '${response}' | jq -r '.component.measures[0].value'", 
                        returnStdout: true
                    ).trim().toDouble()

                    echo "Coverage: ${coverage}"

                    if(coverage < coverageThreshold){
                        error "Code coverage ${coverage}% is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
                    } else {
                        echo "Code coverage ${coverage}% meets the threshold of ${coverageThreshold}%"
                    }
                }
            }
        }

        stage('Build and Push Docker Image'){
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker build -t arnab2903/restaurant-listing-service:${VERSION} .'
                sh 'docker push arnab2903/restaurant-listing-service:${VERSION}'  
            }
        }

        stage('Cleanup Workspace'){
            steps{
                deleteDir()
            }
        }

        stage('Update Image Tag in GitOps') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.com:arnabs-29/deployment-folder.git']])
                script {
                sh '''
                  sed -i "s/image:.*/image: arnab2903\\/restaurant-listing-service:${VERSION}/" aws/restaurant-manifest.yml
                '''
                    sh 'git checkout master'
                    sh 'git add .'
                    sh 'git commit -m "Update image tag"'
            sshagent(['git-ssh'])
                {
                  sh('git push')
                }
            }
        }
    }
    }
}