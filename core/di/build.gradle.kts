plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.inhabitroutine.core.di"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core:database:task:api"))
    implementation(project(":core:database:reminder:api"))
    implementation(project(":core:database:record:api"))

    implementation(project(":core:database:impl"))

    implementation(project(":data:task:api"))
    implementation(project(":data:task:impl"))

    implementation(project(":data:reminder:api"))
    implementation(project(":data:reminder:impl"))

    implementation(project(":data:record:api"))
    implementation(project(":data:record:impl"))
    
    implementation(project(":domain:task:api"))
    implementation(project(":domain:task:impl"))

    implementation(project(":domain:reminder:api"))
    implementation(project(":domain:reminder:impl"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    implementation(libs.bundles.sqldelight)
    implementation(libs.sqldelight.androidDriver)

    implementation(libs.kotlin.serialization)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}