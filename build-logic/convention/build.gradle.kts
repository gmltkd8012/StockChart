plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    jvmTarget.set("17")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    compileOnly(libs.android.tools.build.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("android-hilt") {
            id = "all.hilt"
            implementationClass = "com.leecoder.build_logic.convention.plugin.AndroidHiltPlugin"
        }
    }

    plugins {
        register("application") {
            id = "app.application"
            implementationClass = "com.leecoder.build_logic.convention.plugin.ApplicationConventionPlugin"
        }
    }

    plugins {
        register("application-flavor") {
            id = "app.flavor"
            implementationClass = "com.leecoder.build_logic.convention.plugin.AppFlavorConventionPlugin"
        }
    }

    plugins {
        register("application-compose") {
            id = "app.compose"
            implementationClass = "com.leecoder.build_logic.convention.plugin.ApplicationComposePlugin"
        }
    }

    plugins {
        register("library") {
            id = "lib.library"
            implementationClass = "com.leecoder.build_logic.convention.plugin.LibraryConventionPlugin"
        }
    }

    plugins {
        register("library-flavor") {
            id = "lib.flavor"
            implementationClass = "com.leecoder.build_logic.convention.plugin.LibraryFlavorConventionPlugin"
        }
    }

    plugins {
        register("library-compose") {
            id = "lib.compose"
            implementationClass = "com.leecoder.build_logic.convention.plugin.LibraryComposePlugin"
        }
    }

    plugins {
        register("library-room") {
            id = "lib.room"
            implementationClass = "com.leecoder.build_logic.convention.plugin.AndroidRoomPlugin"
        }
    }
}