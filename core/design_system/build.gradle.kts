plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.lib.compose)
}

android {
    namespace = "com.leecoder.stockchart.design_system"
}

dependencies {
    implementation(project(":core:model"))
}