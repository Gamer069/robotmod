pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Kikugie Snapshots"
            url = uri("https://maven.kikugie.dev/snapshots")
        }
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.10"
}

stonecutter {
    kotlinController.set(true)
    centralScript.set("build.gradle.kts")

    create(getRootProject()) {
        versions("1.21.6", "1.21.8")
        vcsVersion.set("1.21.8")
    }
}

