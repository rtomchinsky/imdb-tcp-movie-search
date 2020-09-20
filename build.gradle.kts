import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "com.tomchinsky.movie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/mockito/maven")
}

dependencies {
    implementation("com.google.inject:guice:4.2.3")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.5.12")
    testImplementation("org.mock-server:mockserver-netty:5.11.1")
    testImplementation("org.mock-server:mockserver-client-java:5.11.1")
    testImplementation("org.mock-server:mockserver-junit-rule:5.11.1")
}

application {
    mainClassName = "com.tomchinsky.imdb.Main"
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
}
