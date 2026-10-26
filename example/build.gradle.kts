plugins {
    id("java")
}

group = "net.flamgop"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.apache.logging.log4j:log4j-core:2.26.1")

    implementation(project(":"))

    implementation("org.slf4j:slf4j-api:2.0.19")
    implementation("org.apache.logging.log4j:log4j-core:2.26.1")
    implementation("org.apache.logging.log4j:log4j-api:2.26.1")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.26.1")
}