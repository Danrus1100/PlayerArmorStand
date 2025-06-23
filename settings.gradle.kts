pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6"
    id("me.modmuss50.mod-publish-plugin") version "0.7.+" apply false
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    shared {
        versions("1.20.1", "1.20.4", "1.20.6", "1.21.1", "1.21.2", "1.21.4", "1.21.5", "1.21.6")
    }
    create(rootProject)
}

rootProject.name = "Template"