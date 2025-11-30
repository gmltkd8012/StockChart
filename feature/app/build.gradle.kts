import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.lib.compose)
}

android {
    namespace = "com.leecoder.stockchart.app"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:design_system"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:util"))
    implementation(project(":core:work"))
    implementation(project(":core:app-config"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.windowsizeclass)
}