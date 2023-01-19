rootProject.name = "Charlatano"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.0"
        id("com.github.johnrengelman.shadow") version "7.1.2"
    }
}
