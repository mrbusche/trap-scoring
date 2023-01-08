plugins {
    id("org.springframework.boot") version "2.4.13"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
    id("org.sonarqube") version "3.5.0.2730"
}

group = "com.trap-scoring"
version = "4.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-test")

    runtimeOnly("mysql:mysql-connector-java")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("ch.vorburger.mariaDB4j:mariaDB4j:2.5.3")
}

sonarqube {
  properties {
    property("sonar.projectKey", "mrbusche_trap-scoring")
    property("sonar.organization", "mrbusche")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}
