plugins {
    `maven-publish`
    id("fabric-loom")
    //id("dev.kikugie.j52j")
    id("me.modmuss50.mod-publish-plugin")
}

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name").toString()
    val version = property("mod.version").toString()
    val group = property("mod.group").toString()
}

class ModDependencies {
    operator fun get(name: String) = property("deps.$name").toString()
}

val mod = ModData()
val deps = ModDependencies()
val mcVersion = stonecutter.current.version
val mcDep = property("mod.mc_dep").toString()

version = "${mod.version}+$mcVersion"
group = mod.group
base { archivesName.set(mod.id) }

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
    }
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }
    maven {
        name = "figuramc"
        url = uri("https://maven.figuramc.org/releases")
    }

    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:$mcVersion+build.${deps["yarn_build"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${deps["fabric_loader"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps["fabric_api"]}")

    if (stonecutter.eval(mcVersion, "1.21.4")) {
        modImplementation(files("../../build/possessive-1.0.3_1.21.4.jar"))
    }


    modApi ("com.terraformersmc:modmenu:${deps["mod_menu"]}")
    modImplementation("dev.isxander:yet-another-config-lib:${deps["yacl"]}")

    implementation("org.quiltmc.parsers:gson:0.2.1")

    if (stonecutter.eval(mcVersion, "1.20.4" )) {
        modImplementation("eu.pb4:placeholder-api:2.4.0-pre.1+1.20.4")
    }
    else if (stonecutter.eval(mcVersion, "1.21.5" )) {
        modImplementation("eu.pb4:placeholder-api:2.6.1+1.21.5")
    }
    else if (stonecutter.eval(mcVersion, "1.21.6")) {
        modImplementation("eu.pb4:placeholder-api:2.7.0+1.21.6")
    }

    if (stonecutter.eval(mcVersion, "1.21.4")) {
        modImplementation("org.figuramc:figura-common-intermediary:0.1.5+1.21.4") //TODO stonecutter versioning
    }
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(true)
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }

    accessWidenerPath = project.rootProject.file("src/main/resources/aws/${stonecutter.current.project}.accesswidener")
}

java {
    withSourcesJar()
    val java = if (stonecutter.eval(mcVersion, ">=1.20.6")) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
    targetCompatibility = java
    sourceCompatibility = java
}

tasks.processResources {
    inputs.property("id", mod.id)
    inputs.property("name", mod.name)
    inputs.property("version", mod.version)
    inputs.property("mcdep", mcDep)
    inputs.property("minecraft_version", stonecutter.current.version.toString())

    val map = mapOf(
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "mcdep" to mcDep,
        "minecraft_version" to stonecutter.current.version.toString()
    )

    filesMatching("fabric.mod.json") { expand(map) }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
    dependsOn("build")
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
    displayName = "${mod.name} ${mod.version}-MC-$mcVersion"
    version = mod.version
    changelog = rootProject.file("CHANGELOG.md").readText()
    type = STABLE
    modLoaders.add("fabric")

    val modrinthToken = providers.gradleProperty("MODRINTH_API_TOKEN").orNull
    val curseforgeToken = providers.gradleProperty("CURSEFORGE_API_TOKEN").orNull

    val discordWebHook = providers.gradleProperty("DISCORD_WEBHOOK").orNull
    val dryDiscordWebHook = providers.gradleProperty("DISCORD_WEBHOOK_DRY").orNull

    dryRun = false

    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = modrinthToken

        type = BETA
        modLoaders.add("fabric")
//        minecraftVersions.add(mcVersion)
        minecraftVersionRange {
            start = property("mod.mc_start").toString()
            end = property("mod.mc_end").toString()
        }
        requires {
            slug = "fabric-api"
        }
        requires{
            slug = "yacl"
        }
        optional{
            slug = "armor-poser"
        }
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = curseforgeToken

        projectSlug = "player-armor-stands"
        type = BETA
        modLoaders.add("fabric")
//        minecraftVersions.add(mcVersion)
        minecraftVersionRange {
            start = property("mod.mc_start").toString()
            end = property("mod.mc_end").toString()
        }
        clientRequired = true
        serverRequired = false
    }

    if (stonecutter.current.version == "1.20.4") {
        discord {
            webhookUrl = discordWebHook
            dryRunWebhookUrl = dryDiscordWebHook

            username  = "Player Armor Stands"
            avatarUrl = "https://github.com/Danrus1100/PlayerArmorStand/blob/main/src/main/resources/assets/pas/icon.png?raw=true"

            content = changelog.map{ "# " + mod.version + " version here! \n\n" + rootProject.file("CHANGELOG.md").readText() +"\n\n<@&1388295587866083338>"}
        }
    }
}
publishing {
    repositories {
        maven("...") {
            name = "..."
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${property("mod.group")}.${mod.id}"
            artifactId = mod.version
            version = mcVersion

            from(components["java"])
        }
    }
}
