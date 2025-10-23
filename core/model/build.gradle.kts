plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.leecoder.stockchart.model"
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
}
