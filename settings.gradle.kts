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
include(":feature:view_schedule")
include(":feature:view_habits")
include(":core:database")
include(":data:task:impl")
include(":data:task:api")
include(":domain:task:impl")
include(":domain:model")
include(":domain:task:api")
include(":core:di")
