plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
//    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":data:task:api"))
    implementation(project(":datasource:task:api"))
    implementation(libs.kotlin.coroutines.core)
//    implementation(libs.bundles.sqldelight)
}