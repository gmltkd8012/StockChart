plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.app.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.app.compose)
}

android {
    namespace = "com.leecoder.stockchart"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.leecoder.stockchart"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:util"))
    implementation(project(":core:work"))
    implementation(project(":feature:app"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
}