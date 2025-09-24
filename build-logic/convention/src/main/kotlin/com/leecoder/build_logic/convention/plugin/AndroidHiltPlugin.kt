package com.leecoder.build_logic.convention.plugin

import com.leecoder.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.dagger.hilt.android")
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                "implementation"(libs.findLibrary("hilt-android").get())
                "implementation"(libs.findLibrary("hilt-navigation-compose").get())
                "ksp"(libs.findLibrary("hilt-android-compiler").get())
            }
        }
    }
}