import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.leecoder.stockchart.data"
}

dependencies {
    ksp (libs.hilt.android.compiler)
    ksp (libs.hilt.compiler)

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:util"))
    implementation(project(":core:room"))
    implementation(project(":core:app-config"))
}