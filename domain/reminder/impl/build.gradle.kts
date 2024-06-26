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
    implementation(project(":domain:reminder:api"))
    implementation(project(":core:platform:reminder:api"))
    implementation(project(":core:util"))
    implementation(project(":data:reminder:api"))
    implementation(project(":data:task:api"))
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.datetime)
}