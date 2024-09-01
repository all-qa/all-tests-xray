pipeline {

    agent {
        kubernetes {
            label 'default'
            defaultContainer 'maven'
            customWorkspace "workspace/${UUID.randomUUID()}"
        }
    }

    parameters {
        choice(description: 'Target browser', choices: ['chrome', 'firefox', 'edge'], name: 'BROWSER')
    }

    environment {
        MAVEN_OPTS = " -Xms512m -Xmx1024m"
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '1', artifactNumToKeepStr: '1'))
        timeout(time: 1, unit: 'HOURS')
        skipDefaultCheckout()
        ansiColor('xterm')
    }

    stages {
        stage("e2e pipeline") {
            steps {
                e2ePipelineStages()
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
 }

def e2ePipelineStages() {

     stage("Checkout e2e tests") {
         checkout scm
     }

     stage("Running e2e tests") {
         sh "mvnd -Dmvnd.idleTimeout=10s clean verify -Dselenium.hub.url=http://selenium-router.selenium-grid.svc.cluster.local:4444 -Dselenium.browser=${params.BROWSER} -Dselenium.target.url=https://google.com"
     }

}