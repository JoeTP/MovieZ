

plugins {
    alias(libs.plugins.android.library)
    kotlin("android")

}

android {
    namespace = "com.example.domain"
    compileSdk = 36
    defaultConfig { minSdk = 24 }

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    implementation(project(":core"))

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}