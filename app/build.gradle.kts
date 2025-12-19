plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.app.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.app.compose)
}

android {
    namespace = "com.leecoder.stockchart"

    defaultConfig {
        applicationId = "com.leecoder.stockchart"
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = libs.versions.appVersionCode.get().toInt()
        versionName = libs.versions.appVersionName.get()

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