plugins {
    java
    alias(libs.plugins.spring.boot)
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.trap-scoring"
version = "10.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation(libs.poi.ooxml)
    implementation(libs.opencsv)

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.web)

    testImplementation(libs.spring.boot.starter.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
