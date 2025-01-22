plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.3"
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
}

group = "com.aider"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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
        untilBuild.set("233.*")
    }

    // Ensure compatibility with Java 17
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
