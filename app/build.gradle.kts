plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.inhabitroutine"
    compileSdk = 34

    signingConfigs {
        this.create("release") {
            storePassword = "sferathebest1917"
            keyPassword = "sferathebest1917"
            keyAlias = "sfera_alias"

        }
    }

    defaultConfig {
        applicationId = "com.example.inhabitroutine"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":feature:view_schedule"))
    implementation(project(":feature:create_edit_task"))
    implementation(project(":feature:view_reminders"))
    implementation(project(":feature:search_tasks"))
    implementation(project(":feature:view_task_statistics"))
    implementation(project(":feature:view_tasks"))
    implementation(project(":feature:view_habits"))

    implementation(project(":core:presentation"))
    implementation(project(":core:util"))
    implementation(project(":core:di"))
    implementation(project(":core:platform:reminder:api"))

    implementation(project(":domain:task:api"))
    implementation(project(":domain:reminder:api"))
    implementation(project(":domain:record:api"))

    implementation(libs.bundles.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}