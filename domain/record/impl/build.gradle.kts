plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":domain:model"))
    implementation(project(":domain:record:api"))
    implementation(project(":data:record:api"))
    implementation(project(":core:util"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.datetime)
}