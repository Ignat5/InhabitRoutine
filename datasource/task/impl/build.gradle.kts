plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":datasource:task:api"))
    implementation(project(":core:database"))
    implementation(project(":data:model"))
    implementation(libs.kotlin.coroutines.core)
}