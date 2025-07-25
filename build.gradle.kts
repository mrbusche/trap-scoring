plugins {
    java
    id("org.springframework.boot") version "4.0.0-M1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.trap-scoring"
version = "9.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

val poiOoxmlVersion = "5.4.1"
val openCsvVersion = "5.11.2"

dependencies {
    implementation("org.apache.poi:poi-ooxml:$poiOoxmlVersion")
    implementation("com.opencsv:opencsv:$openCsvVersion")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
