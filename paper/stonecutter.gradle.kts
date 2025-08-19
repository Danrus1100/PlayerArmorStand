plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.1-paper"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven ("https://repo.papermc.io/repository/maven-public/")
    }
}