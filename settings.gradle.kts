rootProject.name = "SimpleMinecraftAuthLibrary"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include("example")