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
//include(":core:database:api")
include(":core:database:impl")
include(":core:presentation")
include(":feature:view_schedule")
include(":core:database:task:api")
include(":feature:create_edit_task")
include(":core:database:reminder:api")
include(":data:reminder:api")
include(":data:reminder:impl")
include(":domain:reminder:api")
include(":domain:reminder:impl")
include(":feature:view_reminders")
