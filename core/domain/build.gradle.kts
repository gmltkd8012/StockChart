plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.leecoder.stockchart.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:util"))
}