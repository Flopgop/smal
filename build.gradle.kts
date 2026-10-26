plugins {
    id("java-library")
    id("maven-publish")
}

group = "net.flamgop"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.annotations)
    api(libs.msal4j)
    api(libs.okhttp)
    api(libs.moshi)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group as String?
            artifactId = "auth"
            version = project.version as String?
        }
    }
}