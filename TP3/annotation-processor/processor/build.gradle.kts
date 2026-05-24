plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("kapt") // Preferred in Kotlin DSL ( Gradle KTS )
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))

    // AutoService to register the processor automatically
    implementation("com.google.auto.service:auto-service:1.1.1")

    // Required for registering the processor
    kapt("com.google.auto.service:auto-service:1.1.1")

    // KotlinPoet for generating Kotlin code
    implementation("com.squareup:kotlinpoet:1.14.2")

    // Include the annotations module
    implementation(project(":annotations"))
}

kotlin {
    jvmToolchain(23)
}

tasks.test {
    useJUnitPlatform()
}

kapt {
    correctErrorTypes = true
}