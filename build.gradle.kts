plugins {
    java
    id("org.springframework.boot") version "3.3.0-M3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.trap-scoring"
version = "6.0.1"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.apache.poi:poi-ooxml:5.2.5")

    implementation("com.opencsv:opencsv:5.9")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
