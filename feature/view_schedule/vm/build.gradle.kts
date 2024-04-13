plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":core:presentation"))
    implementation(project(":domain:model"))
    implementation(project(":domain:task:api"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.datetime)
}