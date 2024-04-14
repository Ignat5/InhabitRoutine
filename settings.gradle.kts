pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "InhabitRoutine"
include(":app")
include(":core:util")
include(":data:task:impl")
include(":data:task:api")
include(":domain:task:impl")
include(":domain:model")
include(":domain:task:api")
include(":core:di")
include(":core:database:api")
include(":core:database:impl")
//include(":core:presentation_old")
//include(":feature:view_schedule:vm")
//include(":feature:view_schedule:ui")
//include(":core:ui")
include(":core:presentation")
include(":feature:view_schedule")
