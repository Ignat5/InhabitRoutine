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
            this.packageName.set("com.ignatlegostaev.inhabitroutine.core.database.impl")
            this.deriveSchemaFromMigrations = false
            this.schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            this.verifyMigrations = true
        }
    }
}

dependencies {
    implementation(project(":core:database:task:api"))
    implementation(project(":core:database:reminder:api"))
    implementation(project(":core:database:record:api"))
    implementation(project(":core:util"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.bundles.sqldelight)
}