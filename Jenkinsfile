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
				sh "apt-get update \
					apt-get install \
					    ca-certificates \
					    curl \
					    gnupg \
					    lsb-release"
				sh "mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg"
				sh "echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null"
  				sh "apt-get -y install docker-ce docker-ce-cli containerd.io"
				sh "usermod -aG docker sergei270791"
				sh "docker build -t sergei1222/calculador . "
			}
		}
		stage("Docker push") {
			steps {
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
				sleep 60
				sh "chmod +x acceptance_test.sh && ./acceptance_test.sh"
			}
		}
	}
	post {
		always {
			sh "docker stop calculator"
		}
	}
}

		
