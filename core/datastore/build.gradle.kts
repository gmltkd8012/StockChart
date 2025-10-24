plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
}

android {
    namespace = "com.leecoder.stockchart.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}

