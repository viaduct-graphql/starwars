val viaductVersion: String by settings

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // This injects a dynamic value that your TOML can reference.
            version("viaduct", viaductVersion)
        }
    }
}

include(":modules:filmography")
include(":modules:universe")
