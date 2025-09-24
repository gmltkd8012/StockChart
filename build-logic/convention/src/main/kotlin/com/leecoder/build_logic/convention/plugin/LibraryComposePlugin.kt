package com.leecoder.build_logic.convention.plugin

import com.android.build.api.dsl.LibraryExtension
import com.leecoder.build_logic.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
            }

            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
    }
}