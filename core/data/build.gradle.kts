import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.leecoder.stockchart.data"

    val appKey: String = System.getenv("APP_KEY") ?: gradleLocalProperties(rootDir, providers).getProperty("app_key")
    val appSecret: String = System.getenv("APP_SECRET") ?: gradleLocalProperties(rootDir, providers).getProperty("app_secret")

    defaultConfig {
        buildConfigField("String", "AppKey", appKey)
        buildConfigField("String", "AppSecret", appSecret)
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:util"))
    implementation(project(":core:room"))
}