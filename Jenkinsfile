pipeline {
	agent any
	triggers {
		pollSCM('* * * * *')
	}
	stages {
		stage("Compile") {
			steps {
				sh "./gradlew compileJava"
			}
		}
		stage("Unit test") {
			steps {
				sh "./gradlew test"
			}
		}
		stage("Code coverage") {
			steps {

				sh "./gradlew jacocoTestReport"
				publishHTML (target: [
				reportDir: 'build/reports/jacoco/test/html',
				reportFiles: 'index.html',
				reportName:"JaCoCo Report"
				])
				sh "./gradlew jacocoTestCoverageVerification"
			}
		}
		stage("Static code analysis") {
			steps {
				sh "./gradlew checkstyleMain"
				publishHTML (target: [
				reportDir: 'build/reports/checkstyle/',
				reportFiles: 'main.html',
				reportName:"Checkstyle Report"
				])
			}
		}
		stage("Package") {
			steps {
				sh "./gradlew build"
			}
		}
		stage("Docker build") {
			steps {
				
				sh "docker build -t sergei1222/calculador . "
			}
		}
		stage("Docker push") {
			steps {
				sh "docker login --username sergei1222 --password Slobodan123"
				sh "docker push sergei1222/calculador"
			}
		}
		stage("Deploy to staging") {
			steps {
				sh "docker run -d --rm -p 8765:8080 --name calculador sergei1222/calculador"
			}
		}
		stage("Acceptance test") {
			steps {
				sleep 30
				sh "test $(curl localhost:8765/sum?a=1\&b=2) -eq 3"
			}
		}
	}
	post {
		always {
			sh "docker stop calculator"
		}
	}
}

		
