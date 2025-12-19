import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.app.flavor)
    alias(libs.plugins.all.hilt)
    alias(libs.plugins.app.compose)
}

android {
    namespace = "com.leecoder.stockchart"

    val appKeyAlias = System.getenv("APP_KEY_ALIAS") ?: gradleLocalProperties(rootDir, providers).getProperty("app_key_alias")
    val appKeyPassword = System.getenv("APP_KEY_PASSWORD") ?: gradleLocalProperties(rootDir, providers).getProperty("app_key_pw")

    defaultConfig {
        applicationId = "com.leecoder.stockchart"
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = libs.versions.appVersionCode.get().toInt()
        versionName = libs.versions.appVersionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_KEY_ALIAS", "\"$appKeyAlias\"")
        buildConfigField("String", "APP_KEY_PASSWORD", "\"$appKeyPassword\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("../stockchart.keystore.jks")
            storePassword = appKeyPassword
            keyAlias = appKeyAlias
            keyPassword = appKeyPassword
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.configureEach {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val today = dateFormat.format(Date())
        val appName = "bollinger_alarm"
        val versionName = defaultConfig.versionName.orEmpty()

        this.outputs.configureEach {
            buildOutputs.forEach { _ ->
                val archivesBaseName = "$appName-$flavorName-${buildType.name}-v$versionName-$today.apk"
                val variantOutputImpl = this as BaseVariantOutputImpl
                variantOutputImpl.outputFileName = archivesBaseName
            }
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