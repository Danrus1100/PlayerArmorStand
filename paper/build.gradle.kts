import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

val paperServerDir = layout.buildDirectory.dir("paper-server").get().asFile

plugins {
    id("java")
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2")
    implementation(project(":common"))
    compileOnly("io.papermc.paper:paper-api:${property("deps.paper")}")
}

tasks.processResources {
    filesMatching("**/plugin.yml") {
        expand(
            mapOf(
                "api-version" to property("deps.apiVersion")
            )
        )
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.register<Exec>("paperServer") {
    group = "project"
    description = "Builds the Paper server jar and copies it to the build directory."

    doFirst {
        paperServerDir.mkdirs()
    }

    commandLine("gradlew", "paperclip")
    workingDir(paperServerDir)
}