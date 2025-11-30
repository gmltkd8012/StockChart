pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
        }
    }
}

rootProject.name = "StockChart"
include(":app")
include(":core:network")
include(":core:data")
include(":core:model")
include(":feature:app")
include(":core:datastore")
include(":core:ui")
include(":core:design_system")
include(":core:util")
include(":core:domain")
include(":core:room")
include(":core:work")
include(":core:app-config")
