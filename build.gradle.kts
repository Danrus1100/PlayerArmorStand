plugins {
    id("dev.isxander.modstitch.base") version "0.7.0-unstable"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String
val loader: String = name.split("-")[1]
val loaderInitials: String = when (loader) {
    "fabric" -> "FBR"
    "neoforge" -> "NFG"
    "forge" -> "FG"
    "vanilla" -> "VNL"
    else -> throw IllegalArgumentException("Unknown loader: $loader")
}


modstitch {
    minecraftVersion = minecraft

    // Alternatively use stonecutter.eval if you have a lot of versions to target.
    // https://stonecutter.kikugie.dev/stonecutter/guide/setup#checking-versions
    val j21: Boolean = stonecutter.eval(minecraft, ">=1.20.6")
    javaVersion = if (j21) 21 else 17

    // If parchment doesn't exist for a version, yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    var versionName = "${property("mod.version")}-${loaderInitials}-${minecraft}"
    val gitBranchName = "git rev-parse --abbrev-ref HEAD"
        .run { Runtime.getRuntime().exec(this).inputStream.bufferedReader().readText().trim() }
    if (!gitBranchName.equals("main")) {
        versionName += "-$gitBranchName"
    }
    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "pas"
        modName = "Player Armor Stands"
        modVersion = versionName
        modGroup = "com.danrus.pas"
        modAuthor = "Danrus110_"
        modDescription = "Make named armor stands looks like players!"
        modLicense = "MIT"

        fun MapProperty<String, String>.populate(block: MapProperty<String, String>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://discord.com/invite/sBpHZUBebQ")
            put("minecraft_versions", property("version.minecraft") as String)
        }
    }

    // Fabric Loom (Fabric)
    loom {
        fabricLoaderVersion = "0.16.10"

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.all {
                ideConfigGenerated(environment == "client")
                runDir("../../run")
            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.forge") { forgeVersion = it }
        prop("deps.neoforge") { neoForgeVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns(server = false)

        // If you want to use the legacy MDG, you can use the following line:

        // This block configures the `neoforge` extension that MDG exposes by default,
        // you can configure MDG like normal from here
        configureNeoForge {
            runs.all {
                gameDirectory = layout.projectDirectory.dir("../../run")
            }
        }
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("pas") {side = CLIENT}
        // Most of the time you won't ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.split("-")[1]
stonecutter {
    consts(
        "fabric" to constraint.equals("fabric"),
        "neoforge" to constraint.equals("neoforge"),
        "forge" to constraint.equals("forge"),
        "vanilla" to constraint.equals("vanilla")
    )
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitch.loom {
        modstitchModApi("com.terraformersmc:modmenu:${property("deps.modmenu")}")
//        modstitchModImplementation("maven.modrinth:skinshuffle:${property("deps.shuffle")}")
    }
    // Anything else in the dependencies block will be used for all platforms.
    modstitchModApi("dev.architectury:architectury-${property("deps.arch")}")
    modstitchModImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
    modstitchModImplementation("com.mrbysco.armorposer:ArmorPoser-${loader}-${property("deps.armorposer")}") //TODO:Зависит от Cloth Config
}

publishMods {
    val modrinthToken = findProperty("modrinth-token")
    val curseforgeToken = findProperty("curseforge-token")
    val discordWebhook = findProperty("discord-webhook")
    val discordWebhookDry = findProperty("discord-webhook-dry")

    dryRun = false

    modstitch.onEnable {
        file = modstitch.finalJarTask.flatMap { it.archiveFile }
    }

    changelog = rootProject.file("CHANGELOG.md").readText()
    type = BETA

    val loaders = property("pub.target.platforms").toString().split(' ')
    loaders.forEach(modLoaders::add)
    displayName = "Player Armor Stands ${property("mod.version")} for ${loader} ${minecraft}"
    version = "${property("mod.version")}-${loaderInitials}-${minecraft}"

    val targets = property("pub.target.versions").toString().split(' ')
    val requiresLibs = property("pub.libs.required").toString().split(' ')
    val optionalLibs = property("pub.libs.optional").toString().split(' ')
    modrinth {
        projectId = property("publish.modrinth").toString()
        accessToken = modrinthToken.toString()
        targets.forEach(minecraftVersions::add)
        requiresLibs.forEach{requires(it)}
        optionalLibs.forEach{optional(it)}
    }

    curseforge {
        projectId = property("publish.curseforge").toString()
        accessToken = curseforgeToken.toString()
        projectSlug = "player-armor-stands"
        targets.forEach(minecraftVersions::add)
        requiresLibs.forEach{requires(it)}
        optionalLibs.forEach{optional(it)}
    }

    if (targets.contains("1.21.4") && loaders.contains("fabric")) {
        discord {
            webhookUrl = discordWebhook.toString()
            dryRunWebhookUrl = discordWebhookDry.toString()

            username  = "Player Armor Stands"
            avatarUrl = "https://github.com/Danrus1100/PlayerArmorStand/blob/main/src/main/resources/assets/pas/icon.png?raw=true"

            content = changelog.map{ "# " + findProperty("mod.version") + " version here! \n\n" + rootProject.file("CHANGELOG.md").readText() +"\n\n<@&1388295587866083338>"}
        }
    }
}