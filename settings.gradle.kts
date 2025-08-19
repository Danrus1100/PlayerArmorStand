import org.gradle.kotlin.dsl.stonecutter

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
        maven("https://repo.papermc.io/repository/maven-public/")

        // Modstitch, YACL
        maven("https://maven.isxander.dev/releases/")

        // Loom platform
        maven("https://maven.fabricmc.net/")

        // MDG platform
        maven("https://maven.neoforged.net/releases/")

        // Stonecutter
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")

        // Modstitch
        maven("https://maven.isxander.dev/releases")

        // Terraformers (Fabric, ModMenu)
        maven ("https://maven.terraformersmc.com/releases/")

        // Kotlin for Forge (YACL dependencies)
        maven ("https://thedarkcolour.github.io/KotlinForForge/")

        // Architectury
        maven ("https://maven.architectury.dev/")

        // Armor Poser
        maven ("https://maven.saps.dev/releases")

        // Modrinth (SkinShuffle)
        maven ("https://api.modrinth.com/maven")
        mavenCentral()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6.+"
}

include("common")
include("mod")
//include("paper")

stonecutter {
    create(project(":mod")) {
        /**
         * @param mcVersion The base minecraft version.
         * @param loaders A list of loaders to target, supports "fabric" (1.14+), "neoforge"(1.20.6+), "vanilla"(any) or "forge"(<=1.20.1)
         */
        fun mc(mcVersion: String, name: String = mcVersion, loaders: Iterable<String>) =
            loaders.forEach { vers("$name-$it", mcVersion) }

        // Configure your targets here!
        mc("1.21.8", loaders = listOf("neoforge"))
        mc("1.21.6", loaders = listOf("fabric"))
        mc("1.21.4", loaders = listOf("fabric", "neoforge"))
        mc("1.21.1", loaders = listOf("fabric", "neoforge"))
    }
}

rootProject.name = "Player Armor Stands"