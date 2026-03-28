import net.neoforged.moddevgradle.dsl.NeoForgeExtension

plugins {
    java
    id("net.neoforged.moddev") version "2.0.141"
}

val modId: String by project
val modVersion: String by project
val modGroupId: String by project
val mcVersion = project.property("minecraftVersion") as String
val neoVersion = project.property("neoforgeVersion") as String
val mcVersionRange = project.property("minecraftVersionRange") as String
val neoVersionRange = project.property("neoforgeVersionRange") as String
val loaderVerRange = project.property("loaderVersionRange") as String

version = modVersion
group = modGroupId

base {
    archivesName.set(modId)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

extensions.configure<NeoForgeExtension> {
    version = neoVersion

    parchment {
        mappingsVersion = "2024.11.17"
        minecraftVersion = mcVersion
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }
        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

repositories {}
dependencies {}

tasks.withType<ProcessResources> {
    val replaceProperties = mapOf(
        "minecraft_version"       to mcVersion,
        "minecraft_version_range" to mcVersionRange,
        "neoforge_version"        to neoVersion,
        "neoforge_version_range"  to neoVersionRange,
        "loader_version_range"    to loaderVerRange,
        "mod_id"                  to modId,
        "mod_name"                to project.property("mod_name") as String,
        "mod_license"             to project.property("mod_license") as String,
        "mod_version"             to modVersion,
        "mod_authors"             to project.property("mod_authors") as String,
        "mod_description"         to project.property("mod_description") as String
    )
    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}
