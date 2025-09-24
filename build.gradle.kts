import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project
val minecraft_version: String by project
val loader_version: String by project
val fabric_version: String by project
val yarn_mappings: String by project
val sbl_version: String by project

plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("maven-publish")
    id("dev.kikugie.stonecutter") version "0.7.10"
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/robotmod.accesswidener")
}

version = mod_version
group = maven_group

base {
    archivesName = archives_base_name
}


fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "SmartBrainLib"
                url = uri("https://dl.cloudsmith.io/public/tslat/sbl/maven/")
            }
        }
        filter {
            includeGroup("net.tslat.smartbrainlib")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "customportalapi"
                url = uri("https://maven.kyrptonaught.dev")
            }
        }
        filter {
            includeModule("net.kyrptonaught", "customportalapi") // Specify the module if needed
        }
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")
    modImplementation("net.tslat.smartbrainlib:SmartBrainLib-fabric-1.21.7:${sbl_version}")

    modImplementation("net.kyrptonaught:customportalapi:0.0.1-beta68-1.21.8")
    include("net.kyrptonaught:customportalapi:0.0.1-beta68-1.21.8")
}

tasks.named<ProcessResources>("processResources") {
    inputs.property("version", version)
    inputs.property("minecraft_version", minecraft_version)
    inputs.property("loader_version", loader_version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
			"version" to version,
			"minecraft_version" to minecraft_version,
			"loader_version" to loader_version
		)
    }
}

val targetJavaVersion = 21
tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

tasks.named<Jar>("jar") {
    from("LICENSE") {
        rename { "${it}_${archives_base_name}" }
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = archives_base_name
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
