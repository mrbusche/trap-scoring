plugins {
    java
    id("org.springframework.boot") version "3.4.0-M2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.trap-scoring"
version = "6.2.0"

java.sourceCompatibility = JavaVersion.VERSION_22
java.targetCompatibility = JavaVersion.VERSION_22

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.poi:poi-ooxml:5.3.0")

    implementation("com.opencsv:opencsv:5.9")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
