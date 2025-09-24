package com.leecoder.build_logic.convention.plugin

import com.android.build.api.dsl.LibraryExtension
import com.leecoder.build_logic.convention.configureAppDefault
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
            }
            extensions.configure<LibraryExtension> {
                configureAppDefault(this)
            }
        }
    }
}