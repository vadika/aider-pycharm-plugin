plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.16.1"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "com.aider"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
}

dependencies {
    implementation("org.jetbrains.pty4j:pty4j:0.12.13") {
        exclude(group = "net.java.dev.jna")
    }
    implementation("net.java.dev.jna:jna:5.12.1") {
        isTransitive = true
    }
    implementation("net.java.dev.jna:jna-platform:5.12.1") {
        isTransitive = true
    }
}

configurations.all {
    exclude(group = "org.slf4j", module = "slf4j-api")
}

// Configure Java compatibility
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

intellij {
    version.set("2023.1")
    type.set("PC") // PyCharm Community Edition
    plugins.set(listOf("python-ce")) // Add Python plugin dependency
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    
    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("243.*")
    }

    // Ensure compatibility with Java 17
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
