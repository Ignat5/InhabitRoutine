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
    implementation(project(":data:task:api"))
    implementation(project(":core:database:task:api"))
    implementation(project(":core:util"))
    implementation(project(":domain:model"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.datetime)
}