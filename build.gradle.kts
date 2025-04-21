plugins {
	id("fabric-loom").version("1.10-SNAPSHOT")
	id("maven-publish")
}

loom {
	runs {
		configureEach {
			if (name == "client") {
				programArgs.add("--username=Ladybrine")
				programArgs.add("--uuid=5d66606c-949c-47ce-ba4c-a1b9339ba3c8")
			}
		}
	}
}

version = BuildConfig.modVersion + "+1.20"
group = BuildConfig.mavenGroup

base {
	archivesName.set(BuildConfig.modId)
}

repositories {
	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
	maven {
		name = "Jitpack"
		url = uri("https://jitpack.io")
	}
	maven {
		name = "Shedaniel"
		url = uri("https://maven.shedaniel.me/")
	}
	maven {
		name = "Ladysnake Mods"
		url = uri("https://maven.ladysnake.org/releases")
	}
	maven {
		name = "Mod Menu"
		url = uri("https://maven.terraformersmc.com/")
	}
	maven {
		name = "Quilt"
		url = uri("https://maven.quiltmc.org/repository/release")
	}
	maven {
		name = "Jamie white shirt"
		url  = uri("https://maven.jamieswhiteshirt.com/libs-release")
		content {
			includeGroup("com.jamieswhiteshirt")
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${BuildConfig.minecraftVersion}")
	mappings("net.fabricmc:yarn:${BuildConfig.yarnMappings}:v2")
	modImplementation("net.fabricmc:fabric-loader:${BuildConfig.loaderVersion}")

	implementation("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2")
	annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2")
	include("io.github.llamalad7:mixinextras-fabric:0.5.0-rc.2:slim")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${BuildConfig.fabricVersion}")

	modImplementation("maven.modrinth:origins:${BuildConfig.originsVersion}")
	modApi("com.github.apace100:apoli:${BuildConfig.apoliVersion}")
	modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${BuildConfig.ccaVersion}") {
		exclude("net.fabricmc.fabric-api")
	}
	modApi("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${BuildConfig.ccaVersion}") {
		exclude("net.fabricmc.fabric-api")
	}
	modApi("me.shedaniel.cloth:cloth-config-fabric:${BuildConfig.clothconfigVersion}") {
		exclude("net.fabricmc.fabric-api")
	}
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand(
			"version" to BuildConfig.modVersion,
			"modId" to BuildConfig.modId,
			"modName" to BuildConfig.modName,
			"description" to BuildConfig.description,
			"license" to BuildConfig.license,
			"loaderVersion" to BuildConfig.loaderVersion,
			"minecraftVersion" to BuildConfig.minecraftVersion,
			"minecraftVersionRange" to BuildConfig.minecraftVersionRange
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(17)
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${BuildConfig.modId}"}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = BuildConfig.modId
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}