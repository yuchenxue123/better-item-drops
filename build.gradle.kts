import org.gradle.kotlin.dsl.accessors.runtime.addDependencyTo
import java.util.Properties
import kotlin.apply

plugins {
    kotlin("jvm")
    id("fml-loom")
    id("maven-publish")
}

// Mod Info
val mod_name: String by project
val mod_id: String by project
val mod_version: String by project
val mod_description: String by project
// Loader
val loader_version: String by project
val minecraft_version: String by project
// Dependencies
val kotlin_version: String by project

version = mod_version

base {
    archivesName = mod_name
}

repositories {
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://gitlab.com/api/v4/projects/74192719/packages/maven")
    }
}

val access_widener: String by project

loom {
    accessWidenerPath = file("src/main/resources/$access_widener")
    mergedMinecraftJar()
    fml = File("loader/loader-$loader_version.jar")

    mods {
        create(mod_name) {
            sourceSet(sourceSets.main.get())
        }
    }
}

val packageImplementation = configurations.register("packageImplementation") {
    configurations.implementation.get().extendsFrom(this)
}

fun DependencyHandler.packageImplementation(dependencyNotation: Any) {
    add("packageImplementation", dependencyNotation)
}

fun DependencyHandler.packageImplementation(dependencyNotation: Any, dependencyConfiguration: Action<ExternalModuleDependency>) {
    addDependencyTo(this,"packageImplementation", dependencyNotation, dependencyConfiguration)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.fmlMCPMappings())
    implementation(files(loom.fml.toPath()))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")

    implementation("cute.neko.mite:kawakaze-lib:1.1")
}

val properties = mapOf(
    "id" to mod_id,
    "name" to mod_name,
    "version" to mod_version,
    "description" to mod_description,
    "widener" to access_widener
)

tasks.processResources {
    inputs.property("version", mod_version)

    filesMatching("fml.mod.json") {
        expand(properties)
    }
}

tasks.jar {
    from({
        packageImplementation.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    }) {
        exclude("META-INF/maven/**")
        exclude("META-INF/versions/**")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    from(arrayOf("LICENSE.txt", "NOTICE.txt")) {
        into("META-INF/")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmToolchain(17)
    }
}

// publish to gitlab, you can delete this.

val config = Properties().apply {
    val file = file("config.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val maven_name: String by project
val maven_group: String by project

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = maven_group
            artifactId = maven_name
            version = mod_version
        }
    }

    repositories {
        maven {
            url = uri("https://gitlab.com/api/v4/projects/74192719/packages/maven")

            val private_token: String = config.getProperty("private_token") ?: ""

            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = private_token
            }

            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

