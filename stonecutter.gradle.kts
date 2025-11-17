plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.8-neoforge"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledBuildAndCollect", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven ("https://maven.terraformersmc.com/releases/")
        maven("https://maven.isxander.dev/releases/")
        maven ("https://thedarkcolour.github.io/KotlinForForge/")
        maven("https://maven.quiltmc.org/repository/release/")
        maven ("https://maven.architectury.dev/")
        maven ("https://maven.saps.dev/releases")
        maven ("https://api.modrinth.com/maven")
    }
}