plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.lib.compose)
    alias(libs.plugins.all.hilt)
}

android {
    namespace = "com.leecoder.stockchart.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}