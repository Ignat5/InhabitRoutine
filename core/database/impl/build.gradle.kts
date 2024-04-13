plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqlDelight)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sqldelight {
    databases {
        create("InhabitRoutineDatabase") {
            packageName.set("com.example.inhabitroutine.core.database.impl")
        }
    }
}

dependencies {
    implementation(project(":core:database:api"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.bundles.sqldelight)
}