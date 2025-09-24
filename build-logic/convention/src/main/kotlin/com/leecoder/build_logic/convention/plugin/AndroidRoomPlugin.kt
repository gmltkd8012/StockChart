package com.leecoder.build_logic.convention.plugin

import com.leecoder.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                "implementation"(libs.findLibrary("room-runtime").get())
                "implementation"(libs.findLibrary("room-ktx").get())
                "ksp"(libs.findLibrary("room-compiler").get())
            }
        }
    }
}