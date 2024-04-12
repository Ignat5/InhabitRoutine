plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":datasource:task:api"))
    implementation(project(":core:database"))
    implementation(project(":data:model"))
    implementation(project(":domain:model"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.bundles.sqldelight)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.datetime)
}