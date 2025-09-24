package com.leecoder.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class ApiDimension {
    server,
    version,
}

enum class ApiHost {
    BIZ,
    DEV, // 없음
    QA, // 없음
}

@Suppress("EnumEntryName")
enum class ApiDimensionFlavor(
    val dimension: ApiDimension,
    val apiHost: String? = null,
) {
    ApiBiz(dimension = ApiDimension.server, apiHost = ApiHost.BIZ.name),
    normal(dimension = ApiDimension.version),
    servererror(dimension = ApiDimension.version),
}

val dimensionFlavorList = mutableListOf(ApiDimension.server.name, ApiDimension.version.name)

fun configureFlavorDefault(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: ApiDimensionFlavor) -> Unit = {}
) {
    commonExtension.apply {
        buildFeatures {
            buildConfig = true
        }

        flavorDimensions.addAll(dimensionFlavorList)

        productFlavors {
            ApiDimensionFlavor.values().forEach {
                create(it.name) {
                    dimension = it.dimension.name
                    flavorConfigurationBlock(this, it)
                    if (!it.apiHost.isNullOrEmpty()) {
                        buildConfigField("String", "API_HOST", "\"${it.apiHost}\"")
                    }
                }
            }
        }
    }
}