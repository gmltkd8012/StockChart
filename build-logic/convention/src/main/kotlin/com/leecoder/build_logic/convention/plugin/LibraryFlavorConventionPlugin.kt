package com.leecoder.build_logic.convention.plugin

import com.android.build.gradle.LibraryExtension
import com.leecoder.build_logic.convention.configureFlavorDefault
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryFlavorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureFlavorDefault(this)
            }
        }
    }
}