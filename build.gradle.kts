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
    implementation("org.jetbrains.pty4j:pty4j:0.12.13")
    implementation("net.java.dev.jna:jna:5.13.0") {
        isTransitive = true
    }
    implementation("net.java.dev.jna:jna-platform:5.13.0") {
        isTransitive = true
    }
}

configurations.all {
    resolutionStrategy {
        force("net.java.dev.jna:jna:5.13.0")
        force("net.java.dev.jna:jna-platform:5.13.0")
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
    plugins.set(listOf(
        "python-ce", // Python plugin dependency
        "terminal"   // Terminal plugin dependency
    ))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    
    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("243.*")
        changeNotes.set("""
            Initial release:
            - Aider integration in PyCharm
            - Dedicated tool window
            - Settings configuration
            - Direct CLI tool integration
        """.trimIndent())
    }

    // Ensure compatibility with Java 17
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.5"
    }
}
