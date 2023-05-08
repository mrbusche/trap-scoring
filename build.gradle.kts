plugins {
    java
    id("org.springframework.boot") version "3.1.0-RC2"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.trap-scoring"
version = "5.0.1"


java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.poi:poi-ooxml:5.2.3")

    implementation("com.opencsv:opencsv:5.7.1")

    testCompileOnly("org.junit.jupiter:junit-jupiter-api")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
