package com.leecoder.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().toString().toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        buildFeatures {
            compose = true
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("kotlin").get().toString()
        }

        dependencies {
            // androidx-compose-bom - 모든 Compose 라이브러리들의 Version 을 한번에 관리
            add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))

            // androidx-compose ui 관련
            add("implementation", libs.findLibrary("androidx-compose-ui").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
            add("implementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())

            // androidx-compose material3
            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-compose-material").get())

            // 아래 두 라이브러리는 bom 버전 관리 범위 밖
            // androidx-activity-compose - Activity 에서 사용 가능하도록 Compose와 연결
            add("implementation", libs.findLibrary("androidx-activity-compose").get())
            // androidx-navigation-compose - Jetpack Navigation 컴포넌트와 Compose 연결
            add("implementation", libs.findLibrary("androidx-navigation-compose").get())

            // ktx-core 및 lifcycle
            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())

            // compose coil image url load
            add("implementation", libs.findLibrary("androidx-compose-coil").get())

            add("implementation", libs.findLibrary("accompanist-systemUiController").get())
        }
    }
}