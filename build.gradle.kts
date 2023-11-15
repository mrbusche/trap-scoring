plugins {
    java
    id("org.springframework.boot") version "3.2.0-RC2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.trap-scoring"
version = "5.1.2"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.poi:poi-ooxml:5.2.4")

    implementation("com.opencsv:opencsv:5.8")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
